package lib.jellyfin;

public record SearchHint(
  String ItemId,
  String Id,
  String Name,
  String BackdropImageTag,
  String BackdropImageItemId,
  String Type,
  long RunTimeTicks,
  String MediaType,
  String Album,
  String AlbumId,
  String[] Artists,
  String ChannelId
) {}
