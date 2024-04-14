package lib.jellyfin.models;

public record Search(
  SearchHint[] SearchHints,
  int TotalRecordCount
) {}
