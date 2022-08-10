package fr.rushcubeland.rcbcore.bungee;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.rcbcore.bungee.commands.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AutoCompletion implements Listener {

    @EventHandler(priority = 32)
    public void onTab(TabCompleteEvent e) {
        String[] args = e.getCursor().toLowerCase().split(" ");

        if (args.length >= 1){
            if (args[0].startsWith("/")){
                if (Btp.ALIASES.contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                    if (args.length == 2) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 3 && getSpace(e.getCursor()) == 2) {
                        addSuggestionsPlayers(e, args);
                    }
                }
                if(WhoisCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                }
                if(MuteCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                }
                if(BanCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                }
                if(UnmuteCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                }
                if(PunishGUICommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                }
                if(KickCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                }
                if(ReportCommand.ALIASES.contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if(args.length == 1) {
                        if(p.hasPermission("rcbproxy.report")){
                            e.getSuggestions().add("on");
                            e.getSuggestions().add("off");
                        }
                        else
                        {
                            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                e.getSuggestions().add(all.getName());
                            }
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        if(p.hasPermission("rcbproxy.report")){
                            addSuggestionsReportToogle(e, args);
                        }
                        else
                        {
                            addSuggestionsPlayers(e, args);
                        }
                        return;
                    }
                }
                if(MPCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if(args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsPlayers(e, args);
                        return;
                    }
                }
                if(FriendCommand.ALIASES.contains(args[0].replaceAll("/", "")) && e
                .getCursor().contains(" ")){

                    e.getSuggestions().clear();

                    if(args.length == 1){
                        e.getSuggestions().add("list");
                        e.getSuggestions().add("remove");
                        e.getSuggestions().add("add");
                        e.getSuggestions().add("accept");
                        e.getSuggestions().add("deny");
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsArgFriends(e, args);
                        return;
                    }
                    if(args.length == 2){
                        if(args[1].equalsIgnoreCase("add")){
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                e.getSuggestions().add(all.getName());
                            }
                        }
                    }
                    if (args.length == 3 && getSpace(e.getCursor()) == 2) {
                        addSuggestionsPlayers(e, args);
                    }
                }
                if(PartyCommand.ALIASES.contains(args[0].replaceAll("/", "")) && e
                .getCursor().contains(" ")){

                    ProxiedPlayer p = (ProxiedPlayer) e.getSender();

                    e.getSuggestions().clear();

                    if(args.length == 1){
                        e.getSuggestions().add("list");
                        e.getSuggestions().add("remove");
                        e.getSuggestions().add("add");
                        e.getSuggestions().add("accept");
                        e.getSuggestions().add("deny");
                        e.getSuggestions().add("disband");
                        e.getSuggestions().add("leave");
                        e.getSuggestions().add("lead");
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsArgParty(e, args);
                        return;
                    }
                    if(args.length == 2){
                        if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("rm") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete")){
                            final Optional<AParty> aParty = RcbAPI.getInstance().getAccountParty(p);
                            if(aParty.isPresent() && aParty.get().isInParty()){
                                for(ProxiedPlayer pls : aParty.get().getParty().getPlayers()){
                                    e.getSuggestions().add(pls.getDisplayName());
                                }
                            }
                        }
                        if(args[1].equalsIgnoreCase("add")){
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                e.getSuggestions().add(all.getName());
                            }
                        }
                    }
                    if (args.length == 3 && getSpace(e.getCursor()) == 2) {
                        addSuggestionsPlayers(e, args);
                    }
                }
                if(MaintenanceCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")){

                    e.getSuggestions().clear();

                    if(args.length == 1){
                        e.getSuggestions().add("on");
                        e.getSuggestions().add("off");
                        e.getSuggestions().add("add");
                        e.getSuggestions().add("remove");
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestionsArgMaintenance(e, args);
                        return;
                    }
                    if(args.length == 2){
                        if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("rm")){
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                e.getSuggestions().add(all.getName());
                            }
                        }
                        if(args[1].equalsIgnoreCase("add")){
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                e.getSuggestions().add(all.getName());
                            }
                        }
                    }
                    if (args.length == 3 && getSpace(e.getCursor()) == 2) {
                        addSuggestionsPlayers(e, args);
                    }
                }
            }
        }
    }

    private void addSuggestionsPlayers(TabCompleteEvent e, String[] args) {
        String check = args[args.length - 1];

        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if (all.getName().toLowerCase().startsWith(check)) {
                e.getSuggestions().add(all.getName());
            }
        }
    }

    private void addSuggestionsReportToogle(TabCompleteEvent e, String[] args) {
        String check = args[args.length - 1];
        List<String> argstoogle = Arrays.asList("on", "off");

        for(String s : argstoogle){
            if(s.startsWith(check)){
                e.getSuggestions().add(s);
            }
        }
    }
    
    private void addSuggestionsArgFriends(TabCompleteEvent e, String[] args) {
        String check = args[args.length - 1];
        List<String> argFriend = Arrays.asList("list", "remove", "deny", "accept", "add");

        for(String s : argFriend){
            if(s.startsWith(check)){
                e.getSuggestions().add(s);
            }
        }
    }

    private void addSuggestionsArgParty(TabCompleteEvent e, String[] args) {
        String check = args[args.length - 1];
        List<String> argParty = Arrays.asList("list", "remove", "deny", "accept", "add", "leave", "disband", "lead");

        for(String s : argParty){
            if(s.startsWith(check)){
                e.getSuggestions().add(s);
            }
        }
    }

    private void addSuggestionsArgMaintenance(TabCompleteEvent e, String[] args) {
        String check = args[args.length - 1];
        List<String> argMaitenance = Arrays.asList("on", "off", "add", "remove");

        for(String s : argMaitenance){
            if(s.startsWith(check)){
                e.getSuggestions().add(s);
            }
        }
    }

    public static int getSpace(String s) {
        int space = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                space++;
            }
        }
        return space;
    }


}
