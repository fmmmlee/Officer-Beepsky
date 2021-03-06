package xyz.gupton.nickolas.beepsky.music.commands;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import xyz.gupton.nickolas.beepsky.BotUtils;
import xyz.gupton.nickolas.beepsky.Command;
import xyz.gupton.nickolas.beepsky.music.GuildMusicManager;
import xyz.gupton.nickolas.beepsky.music.MusicHelper;

public class StopCommand implements Command {

  /**
   * Checks things such as prefix and permissions to determine if a commands should be executed.
   *
   * @param guild Guild, guild the message was received from, can be null for PM's.
   * @param author User, the author of the message.
   * @param channel MessageChannel, channel the message was received in.
   * @param message String, the contents of the message received.
   * @return boolean, true if the commands should be executed.
   */
  @Override
  public boolean shouldExecute(Guild guild, User author, MessageChannel channel, String message) {
    if (guild == null) {
      return false;
    }

    // if the bot is not in a voice channel ignore the commands
    try {
      guild.getMemberById(BotUtils.CLIENT.getSelfId().get()).block().getVoiceState().block()
          .getChannel().block();
    } catch (NullPointerException e) {
      return false;
    }

    return (message.toLowerCase().equals(BotUtils.PREFIX + "stop")
        || message.toLowerCase().equals(BotUtils.PREFIX + "clear"));
  }

  /**
   * Checks things such as prefix and permissions to determine if a commands should be executed.
   *
   * @param guild Guild, guild the message was received from, can be null for PM's.
   * @param author User, the author of the message.
   * @param channel MessageChannel, channel the message was received in.
   * @param message String, the contents of the message received.
   */
  @Override
  public void execute(Guild guild, User author, MessageChannel channel, String message) {
    GuildMusicManager musicManager = MusicHelper.getGuildMusicManager(guild.getId());
    MusicHelper.clearQueue(musicManager.getScheduler());
    BotUtils.sendMessage(channel, author, "The queue has been cleared!", "");
  }

  /**
   * Returns the usage string for a commands.
   *
   * @param recipient User, who command is going to, used for permissions checking.
   * @return String, the correct usage for the command.
   */
  @Override
  public String getCommand(User recipient) {
    return "`" + BotUtils.PREFIX + "stop` or `"
        + BotUtils.PREFIX + "clear` - Clears the current queue and leaves the voice channel.";
  }
}
