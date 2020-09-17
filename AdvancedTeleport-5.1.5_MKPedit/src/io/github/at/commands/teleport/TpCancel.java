package io.github.at.commands.teleport;

import fanciful.FancyMessage;
import io.github.at.config.Config;
import io.github.at.config.CustomMessages;
import io.github.at.utilities.PagedLists;
import io.github.at.utilities.TPRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCancel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (Config.isFeatureEnabled("teleport")) {
            if (sender.hasPermission("at.member.cancel")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    // Checks if any players have sent a request at all.
                    if (!TPRequest.getRequestsByRequester(player).isEmpty()) {
                        // Checks if there's more than one request.
                        if (TPRequest.getRequestsByRequester(player).size() > 1) {
                            // If the player has specified the request they're accepting.
                            if (args.length > 0) {
                                Player target = Bukkit.getPlayer(args[0]);
                                // Player is offline
                                if (target == null) {
                                    sender.sendMessage(CustomMessages.getString("Errors.noSuchPlayer"));
                                    return true;
                                } else {
                                    TPRequest request = TPRequest.getRequestByReqAndResponder(target, player);
                                    if (request == null) {
                                        sender.sendMessage(CustomMessages.getString("Error.noRequestsFromPlayer").replaceAll("\\{player}", target.getName()));
                                        return true;
                                    } else {
                                        player.sendMessage(CustomMessages.getString("Info.tpCancel"));
                                        request.getResponder().sendMessage(CustomMessages.getString("Info.tpCancelResponder").replaceAll("\\{player}", player.getName()));
                                        request.destroy();
                                        return true;
                                    }
                                }
                            } else {
                                // This utility helps in splitting lists into separate pages, like when you list your plots with PlotMe/PlotSquared.
                                PagedLists<TPRequest> requests = new PagedLists<>(TPRequest.getRequestsByRequester(player), 8);
                                player.sendMessage(CustomMessages.getString("Info.multipleRequestsCancel"));
                                // Displays the first 8 requests
                                for (TPRequest request : requests.getContentsInPage(1)) {
                                    new FancyMessage()
                                            .command("/tpcancel " + request.getResponder().getName())
                                            .text(CustomMessages.getString("Info.multipleRequestsIndex")
                                                    .replaceAll("\\{player}", request.getResponder().getName()))
                                            .send(player);
                                }
                                if (requests.getTotalPages() > 1) {
                                    player.sendMessage(CustomMessages.getString("Info.multipleRequestsList"));
                                }

                            }
                        } else {
                            TPRequest request = TPRequest.getRequestsByRequester(player).get(0);
                            request.getResponder().sendMessage(CustomMessages.getString("Info.tpCancelResponder").replaceAll("\\{player}", player.getName()));
                            player.sendMessage(CustomMessages.getString("Info.tpCancel"));
                            request.destroy();
                            return true;
                        }
                    } else {
                        sender.sendMessage(CustomMessages.getString("Error.noRequests"));
                        return true;
                    }
                } else {
                    sender.sendMessage(CustomMessages.getString("Error.notAPlayer"));
                }
            }
        } else {
            sender.sendMessage(CustomMessages.getString("Error.featureDisabled"));
            return true;
        }
        return true;
    }
}
