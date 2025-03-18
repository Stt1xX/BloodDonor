package com.bloodlink.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Utils {
    public static <T> Page<T> mergeAndPaginate(Page<T> page1, Page<T> page2, Pageable pageable) {
        // Объединяем контент
        List<T> mergedContent = new ArrayList<>();
        mergedContent.addAll(page1.getContent());
        mergedContent.addAll(page2.getContent());
        int totalElements = mergedContent.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<T> paginatedList = mergedContent.subList(start, end);
        return new PageImpl<>(paginatedList, pageable, totalElements);
    }

    public static <T, U extends Comparable<U>> Page<T> sortPage(Page<T> page, Function<T, U> keyExtractor) {
        List<T> sortedList = new ArrayList<>(page.getContent());
        sortedList.sort(Comparator.comparing(keyExtractor));
        return new PageImpl<>(sortedList, page.getPageable(), page.getTotalElements());
    }

}
