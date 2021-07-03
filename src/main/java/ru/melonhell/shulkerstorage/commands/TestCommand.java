package ru.melonhell.shulkerstorage.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.melonhell.shulkerstorage.terminal.TerminalUtils;

@RequiredArgsConstructor
public class TestCommand implements CommandExecutor {

    private final TerminalUtils terminalUtils;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.getInventory().addItem(terminalUtils.getTerminal());
        return true;
    }
}
