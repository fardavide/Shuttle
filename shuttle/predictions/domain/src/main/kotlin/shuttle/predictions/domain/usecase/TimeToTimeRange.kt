package shuttle.predictions.domain.usecase

import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.plus

internal class TimeToTimeRange {

    operator fun invoke(time: Time, timeSpan: TimeSpan): ClosedRange<Time> {
        val offset = timeSpan / 2
        return time - offset .. time + offset
    }
}

private operator fun Time.minus(span: TimeSpan) = Time(this.encoded - span)
