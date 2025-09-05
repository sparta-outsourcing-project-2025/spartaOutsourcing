package com.example.spartaoutsourcing.domain.search.controller;

import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.domain.search.dto.response.IntegratedSearchResponse;
import com.example.spartaoutsourcing.domain.search.dto.response.TaskContentSearchResponse;
import com.example.spartaoutsourcing.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {
private final SearchService searchService;

    @GetMapping("/search")
    public GlobalApiResponse<IntegratedSearchResponse> integratedSearch(@RequestParam(name = "q") String keyword) {
            return GlobalApiResponse.of(SuccessCode.SUCCESS_SEARCH, searchService.getIntegratedSearchResponseByKeyword(keyword));
    }

    @GetMapping("/tasks/search")
    public GlobalApiResponse<PageResponseDto<TaskContentSearchResponse>> taskPageSearch(
            @RequestParam(name = "q") String keyword,
            @PageableDefault Pageable pageable) {
        return GlobalApiResponse.of(SuccessCode.SUCCESS_SEARCH_TASK, searchService.getTaskPageSearchResponse(keyword, pageable));
    }
}
