package org.authorizationserver.util;

import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

	public static List<String> getMonthNames() {
		return List.of("January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December"
		);
	}

	// Tạo danh sách tháng (1-12)
	public static List<String> getMonths() {
		List<String> months = new ArrayList<>();
		for (int month = 1; month <= 12; month++) {
			months.add(String.valueOf(month));
		}
		return months;
	}

	// Tạo danh sách ngày (1 đến maxDays)
	public static List<String> getDays(int maxDays) {
		List<String> days = new ArrayList<>();
		for (int day = 1; day <= maxDays; day++) {
			days.add(String.valueOf(day));
		}
		return days;
	}

	// Tạo danh sách ngày dựa trên tháng và năm
	public static List<String> getDaysInMonth(int month, int year) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Month must be between 1 and 12");
		}
		if (year < 1900 || year > Year.now().getValue()) {
			throw new IllegalArgumentException("Year must be between 1900 and " + Year.now().getValue());
		}
		YearMonth yearMonth = YearMonth.of(year, month);
		int daysInMonth = yearMonth.lengthOfMonth();
		return getDays(daysInMonth);
	}

	// Tạo danh sách năm (1900 đến năm hiện tại)
	public static List<String> getYears() {
		List<String> years = new ArrayList<>();
		int currentYear = Year.now().getValue();
		for (int year = 1900; year <= currentYear; year++) {
			years.add(String.valueOf(year));
		}
		return years;
	}
}
