package es.upm.etsisi.poo;

import es.upm.etsisi.poo.controller.MatchmakingController;
import es.upm.etsisi.poo.controller.TeamController;
import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.controller.command.*;
import es.upm.etsisi.poo.model.Admin;
import es.upm.etsisi.poo.model.Player;
import es.upm.etsisi.poo.model.SessionSingleton;
import es.upm.etsisi.poo.view.CLI;

import java.util.HashMap;


public class App {

    public static void main(String[] args) {
        UserController userController = new UserController();
        MatchmakingController matchmakingController = new MatchmakingController();
        TeamController teamController = new TeamController();
        TournamentController tournamentController = new TournamentController(matchmakingController,
                userController);
        HashMap<String, Command> Admincommands = new HashMap<>();
        HashMap<String, Command> PlayerCommands = new HashMap<>();
        HashMap<String, Command> Usercommands = new HashMap<>();

        //UserCommands
        LoginCommand loginCommand = new LoginCommand(userController);
        Admincommands.put(loginCommand.toString(), loginCommand);
        PlayerCommands.put(loginCommand.toString(), loginCommand);
        Usercommands.put(loginCommand.toString(), loginCommand);

        LogoutCommand logoutCommand = new LogoutCommand(userController);
        Admincommands.put(logoutCommand.toString(), logoutCommand);
        PlayerCommands.put(logoutCommand.toString(), logoutCommand);
        Usercommands.put(logoutCommand.toString(), logoutCommand);

        TournamentList tournamentList = new TournamentList(tournamentController);
        Admincommands.put(tournamentList.toString(), tournamentList);
        PlayerCommands.put(tournamentList.toString(), tournamentList);
        Usercommands.put(tournamentList.toString(), tournamentList);

        //AdminCommands
        PlayerCreate playerCreate = new PlayerCreate(userController);
        Admincommands.put(playerCreate.toString(), playerCreate);

        TeamCreate teamCreate = new TeamCreate(teamController, userController);
        Admincommands.put(teamCreate.toString(), teamCreate);

        PlayerDelete playerDelete = new PlayerDelete(userController, tournamentController, teamController);
        Admincommands.put(playerDelete.toString(), playerDelete);

        TeamDelete teamDelete = new TeamDelete(teamController, tournamentController);
        Admincommands.put(teamDelete.toString(), teamDelete);

        TeamAdd teamAdd = new TeamAdd(userController, teamController, tournamentController);
        Admincommands.put(teamAdd.toString(), teamAdd);

        TeamRemove teamRemove = new TeamRemove(userController, teamController);
        Admincommands.put(teamRemove.toString(), teamRemove);

        TournamentCreate tournamentCreate = new TournamentCreate(tournamentController);
        Admincommands.put(tournamentCreate.toString(), tournamentCreate);
        TournamentDelete tournamentDelete = new TournamentDelete(tournamentController);
        Admincommands.put(tournamentDelete.toString(), tournamentDelete);
        TournamentMatchmaking tournamentMatchmaking = new TournamentMatchmaking(tournamentController);
        Admincommands.put(tournamentMatchmaking.toString(), tournamentMatchmaking);

        //Player Commands
        TournamentAdd tournamentAdd = new TournamentAdd(tournamentController, userController, teamController);
        PlayerCommands.put(tournamentAdd.toString(), tournamentAdd);

        TournamentRemove tournamentRemove = new TournamentRemove(tournamentController);
        PlayerCommands.put(tournamentRemove.toString(), tournamentRemove);

        StatisticShow statisticShow = new StatisticShow(userController);
        PlayerCommands.put(statisticShow.toString(), statisticShow);

        //Admin registration:
        userController.createAdmin("Pafu", "12345678", "Álvaro", "Ribas");
        userController.createAdmin("Dani", "12345678", "Daniel", "Rivera");
        userController.createAdmin("DaniCLI", "12345678", "Daniel", "Bermúdez");
        userController.createAdmin("APerez", "12345678", "Álvaro", "Pérez");

        CLI cli = new CLI();
        String[] input;
        String[] content;
        boolean end = false;
        HashMap<String, Command> commands;
        String userCommands =
                """
                        login username;password
                        logout
                        tournament-list
                        """.strip();
        String adminCommands =
                """
                        player-create username;password;forename;surname;DNI
                        team-create name
                        player-delete username
                        team-delete teamname
                        team-add username;teamName
                        team-remove username;teamName
                        tournament-create name;initialDate;endDate;league;sport;TCategory
                        tournament-delete name
                        tournament-matchmaking -m;participant1Name;participant2Name;tournamentName
                        tournament-matchmaking -a;tournamentName
                        """;
        String playerCommands =
                """
                        tournament-add tournamentName
                        tournament-add tournamentName;teamName
                        tournament-remove tournamentName
                        tournament-remove teamName;tournamentName
                        statistics-show -csv/-json
                        """;
        while (!end) {
            System.out.println("Commands:\n");
            System.out.println(userCommands);

            if (userController.getLoggedUser() instanceof Player) {
                System.out.println(playerCommands);
                commands = PlayerCommands;
            } else if (userController.getLoggedUser() instanceof Admin) {
                System.out.println(adminCommands);
                commands = Admincommands;
            } else commands = Usercommands;

            input = cli.split(cli.readCommand(), " ");

            if (input.length >= 1) {
                if (input[0].equalsIgnoreCase("exit")) {
                    SessionSingleton.shutdown();
                    end = true;
                } else if (commands.containsKey(input[0])) {
                    if (input.length > 1)
                        content = cli.split(input[1], ";");
                    else content = new String[0];
                    System.out.println(commands.get(input[0]).apply(content));
                } else System.out.println("Command not found\n");
            }
        }
    }
}
