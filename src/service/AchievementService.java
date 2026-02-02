package service;

import models.Achievement;
import models.Rental;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class AchievementService {

    public List<Achievement> checkAchievements(User user, int ordersCount, int rentalsCount, int reviewsCount, List<Rental> rentals) {
        List<Achievement> achievements = new ArrayList<>();

        // First Steps
        if (ordersCount >= 1) {
            achievements.add(new Achievement(
                    "First Steps",
                    "Made your first purchase",
                    2
            ));
        }

        // Book Collector - 10+ orders
        if (ordersCount >= 10) {
            achievements.add(new Achievement(
                    "Book Collector",
                    "Purchased 10 books",
                    4
            ));
        }

        // Active Reader - 5+ rentals
        if (rentalsCount >= 5) {
            achievements.add(new Achievement(
                    "Active Reader",
                    "Rented 5 books",
                    3
            ));
        }

        // Literary Critic - 5+ reviews
        if (reviewsCount >= 5) {
            achievements.add(new Achievement(
                    "Literary Critic",
                    "Wrote 5 reviews",
                    5
            ));
        }

        // TIME-BASED ACHIEVEMENTS
        boolean hasEarlyBird = false;
        boolean hasDayReader = false;
        boolean hasEveningReader = false;
        boolean hasNightOwl = false;

        for (Rental rental : rentals) {
            int hour = rental.getRentedAt().getHour();

            // Early Bird: 5:00 - 10:59
            if (hour >= 5 && hour <= 10 && !hasEarlyBird) {
                achievements.add(new Achievement(
                        "Early Bird 🌅",
                        "Rented a book in the morning (5:00-10:59)",
                        3
                ));
                hasEarlyBird = true;
            }

            // Day Reader: 11:00 - 16:59
            if (hour >= 11 && hour <= 16 && !hasDayReader) {
                achievements.add(new Achievement(
                        "Day Reader ☀️",
                        "Rented a book during the day (11:00-16:59)",
                        2
                ));
                hasDayReader = true;
            }


            // Evening Reader: 17:00 - 21:59
            if (hour >= 17 && hour <= 21 && !hasEveningReader) {
                achievements.add(new Achievement(
                        "Evening Reader 🌆",
                        "Rented a book in the evening (17:00-21:59)",
                        3
                ));
                hasEveningReader = true;
            }

            // Night Owl: 22:00 - 4:59
            if ((hour >= 22 || hour <= 4) && !hasNightOwl) {
                achievements.add(new Achievement(
                        "Night Owl 🌙",
                        "Rented a book at night (22:00-4:59)",
                        5
                ));
                hasNightOwl = true;
            }
        }

        return achievements;
    }
}
