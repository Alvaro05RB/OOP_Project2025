package es.upm.etsisi.poo.controller.command;

import es.upm.etsisi.poo.controller.TeamController;
import es.upm.etsisi.poo.controller.TournamentController;
import es.upm.etsisi.poo.controller.UserController;
import es.upm.etsisi.poo.exceptions.CommandException;
import es.upm.etsisi.poo.exceptions.InvalidParameterException;
import es.upm.etsisi.poo.exceptions.ObjectNotFoundException;
import es.upm.etsisi.poo.model.Player;
import es.upm.etsisi.poo.model.Team;

public class TeamAdd implements Command {
    private final UserController playerController;
    private final TeamController teamController;
    private final TournamentController tournamentController;

    public TeamAdd(UserController playerController, TeamController teamController,
                   TournamentController tournamentController) {
        this.playerController = playerController;
        this.teamController = teamController;
        this.tournamentController = tournamentController;
    }
/*
    @Override
    public String apply(String[] params) {
        String result;
        if (params.length == 2) {
            Player player = playerController.getPlayer(params[0]);
            if (player != null) {
                Team team = teamController.get(params[1]);
                if (team != null) {
                    if (!teamController.playerIsInTeam(player, params[1])) {
                        if (!tournamentController.playerAndTeamIntersect(tournamentController.getTournaments(player), tournamentController.getTournaments(team))
                                && teamController.addTeam(player, params[1])) {
                            result = "Player added to team successfully: " + params[0] + " is now in " + params[1];
                        } else {
                            result =
                                    "Player " + params[0] + " participates in a tournament where team " + params[1] + " participates too";
                        }
                    } else {
                        result = "Error: Player " + params[0] + " already exists in " + params[1];
                    }
                } else {
                    result = "Error: Team " + params[1] + " does not exist";
                }
            } else {
                result = "Error: Player with username " + params[0] + " does not exist.";
            }
        } else {
            result = "Incorrect number of params in Command.";
        }
        return result;
    }
    */

    @Override
    public String apply(String[] params) {
        String result;
        try {
            checkParams(2,params.length);
            result = TeamAddCommand(params[0],params[1]);
        }catch (InvalidParameterException | ObjectNotFoundException | CommandException e){
            result = e.getMessage();
        }
        return result;
    }

    private String TeamAddCommand(String param1, String param2)throws ObjectNotFoundException, CommandException {
        Player player = playerController.getPlayer(param1);
        if(player!=null){
            Team team = teamController.get(param2);
            if (team != null) {
                if (!teamController.playerIsInTeam(player, param2)) {
                    if (!tournamentController.playerAndTeamIntersect(tournamentController.getTournaments(player), tournamentController.getTournaments(team))
                            && teamController.addTeam(player, param2)) {
                        return "Player added to team successfully: " + param1 + " is now in " + param2;
                    }else {
                        throw new CommandException(param1,"Player",param2,"Team","","participates in a tournament where the ","participates too");
                    }
                } else {
                    throw new CommandException(param1,"Player",param2,"Team","","is already a part of the ","");
                }
            }else {
                throw new ObjectNotFoundException("Team",param2);
            }
        }else {
            throw new ObjectNotFoundException("Player",param1);
        }
    }
    public String toString() {
        return "team-add";
    }


}
