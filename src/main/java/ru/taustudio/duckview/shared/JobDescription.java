package ru.taustudio.duckview.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class JobDescription {
	Long jobId;
	String url;
	Integer width;
	Integer height;
}
