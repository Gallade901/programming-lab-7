package org.example.server.utility;

import org.example.exceptions.InappropriateFormatException;
import org.example.utility.User;

/**
 * Interface that all commands implement
 */
public interface Command {
    boolean execute(String argument, Object commandObjectArgument, User user);
    String description();
    String getName();
}
