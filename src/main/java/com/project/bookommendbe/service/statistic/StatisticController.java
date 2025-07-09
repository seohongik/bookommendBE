package com.project.bookommendbe.service.statistic;

import com.project.bookommendbe.db.StatisticMonthlyBookCategoryRepository;
import com.project.bookommendbe.db.StatisticMonthlyBookMoneyRepository;
import com.project.bookommendbe.db.StatisticMonthlyReadCountRepository;
import com.project.bookommendbe.dto.MonthlyBookCategory;
import com.project.bookommendbe.dto.MonthlyBookMoney;
import com.project.bookommendbe.dto.MonthlyReadCount;
import com.project.bookommendbe.dto.StatisticVO;
import com.project.bookommendbe.entity.BookCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticController {

    private final StatisticMonthlyReadCountRepository statisticMonthlyReadCountRepository;
    private final StatisticMonthlyBookMoneyRepository statisticMonthlyBookMoneyRepository;
    private final StatisticMonthlyBookCategoryRepository statisticMonthlyBookCategoryRepository;

    @GetMapping("/r1/statistic/monthly-read-count")
    public List<StatisticVO> statisticReadCount(@RequestParam Long userId, @RequestParam int year) {

        List<StatisticVO> statisticVOS = new ArrayList<>();

        List<MonthlyReadCount> dbMonthlyReadCounts=statisticMonthlyReadCountRepository.findMonthlyReadCountByUserIdAndYear(userId, year);
        int endMonth =12;
        for (int i=1; i<=endMonth; i++) {

            StatisticVO statisticVO = new StatisticVO();
            for (MonthlyReadCount item : dbMonthlyReadCounts) {
                if (item.getMonths().equals(String.valueOf(i))) {
                    statisticVO.setKey(String.valueOf(item.getReadCount()));
                    statisticVO.setData(item.getMonths());
                } else {
                    statisticVO.setKey("0");
                    statisticVO.setData(String.valueOf(i));
                }
            }
            statisticVOS.add(statisticVO);
        }

        return statisticVOS;

        //return statisticRepository.findMonthlyReadCountByUserIdAndYear(userId, year);
    }

    @GetMapping("/r1/statistic/monthly-book-money")
    public List<StatisticVO> statisticBookMoney(@RequestParam Long userId, @RequestParam int year) {


        List<StatisticVO> statisticVOS = new ArrayList<>();

        List<MonthlyBookMoney> dbMonthlyBookMoney=statisticMonthlyBookMoneyRepository.findMonthlyBookMoneyByUserIdAndYear(userId, year);
        int endMonth =12;
        for (int i=1; i<=endMonth; i++) {

            StatisticVO statisticVO = new StatisticVO();
            for (MonthlyBookMoney item : dbMonthlyBookMoney) {
                if (item.getMonths().equals(String.valueOf(i))) {
                    statisticVO.setKey(item.getMonths());
                    statisticVO.setData(String.valueOf(item.getDiscount()));
                } else {
                    statisticVO.setKey(String.valueOf(i));
                    statisticVO.setData("0");
                }
            }
            statisticVOS.add(statisticVO);
        }

        return statisticVOS;
    }

    @GetMapping("/r1/statistic/monthly-book-category")
    public List<StatisticVO> statisticBookCategory(@RequestParam Long userId, @RequestParam int year) {


        String[] categoriesList = {
                "GENERALITIES"
                , "PHILOSOPHY"
                , "RELIGION"
                , "SOCIAL_SCIENCE"
                , "NATURAL_SCIENCE"
                , "TECHNOLOGY"
                , "FINE_ARTS"
                , "LANGUAGE"
                , "LITERATURE"
                , "HISTORY"
        };

        Map<String,String> categoryMap=new HashMap<>();

        categoryMap.put(categoriesList[0], "#FFFFFF");
        categoryMap.put(categoriesList[1], "#B5B2FF");
        categoryMap.put(categoriesList[2], "#FFD1FF");
        categoryMap.put(categoriesList[3], "#FFFFC5");
        categoryMap.put(categoriesList[4], "#FFBA85");
        categoryMap.put(categoriesList[5], "#D5D5D5");
        categoryMap.put(categoriesList[6], "#FFF136");
        categoryMap.put(categoriesList[7], "#D7FF6C");
        categoryMap.put(categoriesList[8], "#FFA7A7");
        categoryMap.put(categoriesList[9], "#C07F5A");


        List<StatisticVO> statisticVOS = new ArrayList<>();

        List<MonthlyBookCategory> dbMonthlyBookMoney=statisticMonthlyBookCategoryRepository.findMonthlyBookMoneyByUserIdAndYear(userId, year);

        Set<BookCategory> categories = EnumSet.allOf(BookCategory.class);

        for (BookCategory category: categories){
            StatisticVO statisticVO = new StatisticVO();
            for (MonthlyBookCategory item : dbMonthlyBookMoney) {
                if(item.getCategory().equals(category.getCategory())) {
                    statisticVO.setKey(String.valueOf(item.getCategoryCount()*10));
                    statisticVO.setData(categoryMap.get(item.getCategory()));
                    statisticVO.setEtc(category.getCategory());
                }else {
                    statisticVO.setKey(String.valueOf("10"));
                    statisticVO.setData("black");
                    statisticVO.setEtc("");
                }
            }
            statisticVOS.add(statisticVO);
        }

        return statisticVOS;
    }
}
