package lib.jellyfin;

public record Search(
  SearchHint[] SearchHints,
  int TotalRecordCount
) {}
