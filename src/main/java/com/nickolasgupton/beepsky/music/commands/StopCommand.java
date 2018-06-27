package com.nickolasgupton.beepsky.music.commands;

import com.nickolasgupton.beepsky.BotUtils;
import com.nickolasgupton.beepsky.Command;
import com.nickolasgupton.beepsky.music.GuildMusicManager;
import com.nickolasgupton.beepsky.music.MusicHelper;
import java.awt.Color;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.EmbedBuilder;

public class StopCommand implements Command {

  /**
   * Checks things such as prefix and permissions to determine if a commands should be executed.
   *
   * @param message The message received.
   * @return True if the commands should be executed.
   */
  @Override
  public boolean shouldExecute(IMessage message) {
    if (message.getChannel().isPrivate()) {
      return false;
    }

    IVoiceChannel botVoiceChannel = BotUtils.CLIENT.getOurUser()
        .getVoiceStateForGuild(message.getGuild()).getChannel();

    // if the bot is not in a voice channel ignore the commands
    if (botVoiceChannel == null) {
      return false;
    }

    return (message.toString().toLowerCase().equals(BotUtils.PREFIX + "stop")
        || message.toString().toLowerCase().equals(BotUtils.PREFIX + "clear"));
  }

  /**
   * Executes the commands if it exists.
   *
   * @param event Provided by D4J.
   */
  @Override
  public void execute(MessageReceivedEvent event) {
    GuildMusicManager musicManager = MusicHelper.getGuildAudioPlayer(event.getGuild());
    MusicHelper.clearQueue(musicManager.getScheduler());

    IVoiceChannel botVoiceChannel = BotUtils.CLIENT.getOurUser()
        .getVoiceStateForGuild(event.getGuild()).getChannel();
    botVoiceChannel.leave();

    EmbedBuilder message = new EmbedBuilder();
    message.withColor(Color.green);
    message.withTitle("The queue has been cleared!");
    BotUtils.sendMessage(event.getChannel(), event.getAuthor(), message);
  }

  /**
   * Returns the usage string for a commands.
   *
   * @return String of the correct usage for the commands.
   */
  @Override
  public String getCommand(IUser recipient) {
    return "`" + BotUtils.PREFIX + "stop` or `"
        + BotUtils.PREFIX + "clear` - Clears the current queue and leaves the voice channel.";
  }
}