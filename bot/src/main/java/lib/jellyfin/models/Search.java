package lib.jellyfin.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Search(
  SearchHint[] SearchHints,
  int TotalRecordCount
) {}
