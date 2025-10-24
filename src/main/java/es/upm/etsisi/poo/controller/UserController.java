package es.upm.etsisi.poo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import es.upm.etsisi.poo.model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public class UserController {
    private User loggedUser;

    public UserController() {
        loggedUser = null;
    }

    public boolean login(String username, String psw) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            String hashedPassword = hashPassword(psw);
            if (user != null && user.getPassword().equals(hashedPassword)) {
                loggedUser = user;
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean logout() {
        boolean result = false;
        if (loggedUser != null) {
            loggedUser = null;
            result = true;
        }
        return result;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public Player getPlayer(String username) {
        Player player = null;
        Transaction transaction = null;

        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            player = session.createQuery("FROM Player WHERE username = :username", Player.class)
                    .setParameter("username", username)
                    .uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return player;
    }

    public boolean removeUser(String username) {
        boolean result = false;

        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (user != null) {
                if(user instanceof Player){
                    Participant participant = (Player) user;
                    ParticipantBase participantBase = ParticipantBase.getParticipantFromParticipant(participant);
                    if(participantBase!=null) {
                        session.delete(participantBase);
                    }
                }
                session.delete(user);
                result = true;
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean createPlayer(String username, String password, String forename, String surname, String DNI) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User existingUser = session.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (existingUser == null) {
                String hashedPassword = hashPassword(password);
                Player player = new Player(username,
                        hashedPassword,
                        forename,
                        surname,
                        DNI,
                        (Admin) getLoggedUser());
                session.save(player);
                ParticipantBase participant = new ParticipantBase(player);
                session.save(participant);
                result = true;
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean createAdmin(String username, String pwd, String forename, String surname) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Admin existingAdmin = session.createQuery("SELECT a FROM Admin a WHERE a.username = :username", Admin.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (existingAdmin == null) {
                String hashedPassword = hashPassword(pwd);
                Admin newAdmin = new Admin(username, hashedPassword, forename, surname);
                session.save(newAdmin);
                transaction.commit();
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean exists(String username) {
        boolean result = false;
        try (Session session = SessionSingleton.getSessionFactory().openSession()) {
            User existingUser = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (existingUser != null) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String showStatistics() {
        StringBuilder result = new StringBuilder();
        if (getLoggedUser() instanceof Player) {
            try (Session session = SessionSingleton.getSessionFactory().openSession()) {
                Player player = session.createQuery("FROM Player WHERE username = :username", Player.class)
                        .setParameter("username", loggedUser.getUsername())
                        .uniqueResult();
                if (player != null) {
                    result.append("Statistics for ").append(player.getUsername()).append(":\n");
                    Set<Statistic> set = player.getStatistics();
                    for (Statistic statistic : set) {
                        result.append(statistic.getCategory().toString()).append(": ").append(statistic.getScore())
                                .append("\n");
                    }
                    result.append("Total: ").append(player.getScore()).append("\n");
                    result.append("Average: ").append(player.getScore() / set.size()).append("\n");
                } else {
                        result.append("Error: Player not found in the database.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.append("Error: Unable to fetch player statistics.");
            }
        } else {
            result.append("Error: Current user is not a player.");
        }

        return result.toString();
    }

    public String toCSV(String filename) {
        String result = "";
        if (getLoggedUser() instanceof Player player) {
            boolean IOFlag = false;
            try (FileWriter fileWriter = new FileWriter(filename)) {
                fileWriter.append("Statistics for ").append(player.getUsername()).append(":");
                Set<Statistic> set = ((Player) getLoggedUser()).getStatistics();
                for (Statistic statistic : set) {
                    fileWriter.append(statistic.getCategory().toString()).append(": ")
                            .append(statistic.getScore().toString());
                }
                fileWriter.append("\nTotal: ").append(String.valueOf(((Player) getLoggedUser()).getScore())).append(
                        "\nAverage: ").append(String.valueOf((((Player) getLoggedUser()).getScore()) / set.size()));
            } catch (IOException e) {
                result = e.getMessage();
                IOFlag = true;
            }
            if (!IOFlag) {
                result = "File " + filename + " was created correctly";
            }
        } else {
            result = "Error: current user is not a player";
        }
        return result;
    }


    public String toJson() {
        String result;
        if (getLoggedUser() instanceof Player) {
            String statsText = showStatistics();
            String json = formatToJson(statsText);
            try (FileWriter writer = new FileWriter("player_statistics.json")) {
                writer.write(json);
                result = "JSON file created correctly";
            } catch (IOException e) {
                result = "Error: unable to create JSON file - " + e.getMessage();
            }
        } else {
            result = "Error: current user is not a player";
        }
        return result;
    }
    public static String formatToJson(String input) {
        String[] lines = input.split("\n");
        JsonObject jsonObject = new JsonObject();
        if (lines[0].startsWith("Statistics for")) {
            String playerName = lines[0].substring("Statistics for".length()).trim().replace(":", "");
            jsonObject.addProperty("player", playerName);
        }
        JsonObject statistics = new JsonObject();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty() && line.contains(":")) {
                String[] parts = line.split(":");
                String key = parts[0].trim();
                String value = parts[1].trim();
                try {
                    double numericValue = Double.parseDouble(value);
                    statistics.addProperty(key, numericValue);
                } catch (NumberFormatException e) {
                    statistics.addProperty(key, value);
                }
            }
        }
        jsonObject.add("statistics", statistics);
        return jsonObject.toString();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
