package com.example.spartaoutsourcing.domain.task.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class PageResponseDto<T> {

	//조회할 데이터
	private List<T> content;

	//전체 데이터 개수
	private final long totalElements;

	//총 페이지 수
	private final int totalPages;

	//한 페이지에 담긴 데이터 개수
	private final long size;

	//페이지 번호
	private final long number;

	public PageResponseDto(List<T> content, long totalElements, long size, long number) {
		this.content = content;
		this.totalElements = totalElements;
		this.totalPages = (int)Math.ceil((double)totalElements / size);
		this.size = size;
		this.number = number;
	}

	public static <T> PageResponseDto<T> of(List<T> content, long totalElements, long size, long number) {
		return new PageResponseDto<>(content, totalElements, size, number);
	}
}