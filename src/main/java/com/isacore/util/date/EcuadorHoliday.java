package com.isacore.util.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * The list of Federal Observances, as per section 6103(a) of title 5 of the United States Code.
 *
 * @author James Dunnam
 * @see http://www.law.cornell.edu/uscode/text/5/6103
 */
public enum EcuadorHoliday implements Holiday {
    NUEVO_ANIO(FixedObservance.of(Month.JANUARY, 1)),
    CARNAVAL_1(FixedObservance.of(Month.FEBRUARY, 15)),
    CARNAVAL_2(FixedObservance.of(Month.FEBRUARY, 16)),
    VIERNES_SANTO(FixedObservance.of(Month.APRIL, 2)),
    DIA_TRABAJO(FixedObservance.of(Month.MAY, 1)),
    BATALLA_PICHINCHA(FixedObservance.of(Month.MAY, 24)),
    INDEPENDENCIA_ECUADOR(FixedObservance.of(Month.AUGUST, 10)),
    INDEPENDENCIA_GUAYAQUIL(FixedObservance.of(Month.OCTOBER, 9)),
    DIFUNTOS_1(FixedObservance.of(Month.NOVEMBER, 1)),
    DIFUNTOS_2(FixedObservance.of(Month.NOVEMBER, 2)),
    NAVIDAD(FixedObservance.of(Month.DECEMBER, 25));

    private final Holiday delegate;
    static final EcuadorHoliday[] ECUADOR_HOLIDAYS = EcuadorHoliday.values();

    EcuadorHoliday(Holiday delegate) {
        this.delegate = delegate;
    }

    @Override
    public LocalDate getObservanceFor(int year) {
        return delegate.getObservanceFor(year);
    }

    /**
     * Class representing a varying observance of a US federal holiday. e.g. Thanksgiving is celebrated on the fourth Thursday of November.
     *
     * @implNote An {@code ordinal} value of {@literal -1} represents the last week of the month. e.g. "The last Monday in May"
     */
    private static class VaryingObservance extends BaseHoliday {

        private final DayOfWeek dayOfWeek;
        private final int ordinal;

        private VaryingObservance(Month month, DayOfWeek dayOfWeek, int ordinal) {
            super(month);
            this.dayOfWeek = dayOfWeek;
            this.ordinal = ordinal;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LocalDate getActualHolidayDate(int year) {
            return LocalDate.of(year, month, 1)
                            .with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek));
        }

        static VaryingObservance of(int ordinal, DayOfWeek dayOfWeek, Month month) {
            return new VaryingObservance(month, dayOfWeek, ordinal);
        }

    }

    /**
     * Class representing a fixed observance of a US federal holiday. e.g New Years Day is always on January 1
     */
    private static class FixedObservance extends BaseHoliday {
        private final int dayOfMonth;

        private FixedObservance(Month month, int dayOfMonth) {
            super(month);
            this.dayOfMonth = dayOfMonth;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LocalDate getActualHolidayDate(int year) {
            return LocalDate.of(year, month, dayOfMonth);
        }

        static FixedObservance of(Month month, int dayOfMonth) {
            return new FixedObservance(month, dayOfMonth);
        }
    }

    /**
     * Base class for the Federal Holidays. Subclasses should implement {@link #getActualHolidayDate(int)} and this class will adjust for weekends if
     * necessary.
     */
    private static abstract class BaseHoliday implements Holiday {
        final Month month;

        BaseHoliday(Month month) {
            this.month = month;
        }

        /**
         * Get the actual, unadjusted, day of the holiday
         *
         * @param year the year to query for
         *
         * @return the actual day the holiday is celebrated on.
         */
        abstract LocalDate getActualHolidayDate(int year);

        /**
         * {@inheritDoc}
         */
        @Override
        public LocalDate getObservanceFor(int year) {
            final LocalDate actualHolidayDate = getActualHolidayDate(year);
            return adjustForWeekendIfNecessary(actualHolidayDate);
        }

        /**
         * Helper method to adjust the observance day for a holiday that falls on a {@link DayOfWeek#SATURDAY} or {@link DayOfWeek#SUNDAY}.
         * See Executive order 11582, February 11, 1971.
         *
         * @param holiday the actual {@link LocalDate} that the holiday falls on.
         *
         * @return a possibly adjusted {@link LocalDate}
         */
        private LocalDate adjustForWeekendIfNecessary(LocalDate holiday) {
            switch (holiday.getDayOfWeek()) {
                case SATURDAY:
                    return holiday.minus(1, ChronoUnit.DAYS);
                case SUNDAY:
                    return holiday.plus(1, ChronoUnit.DAYS);
                default:
                    return holiday;
            }
        }
    }
}

