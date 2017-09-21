package pro.mickey.snowflake;

import java.util.Calendar;

public class Snowflake {

	// 机器号 长度
	private static final int WORKER_ID_BITS = 10;
	// 序列号长度
	private static final int SEQUENCE_BITS = 12;
	// 机器号和序列号长度
	private static final int WORKER_ID_SEQUENCE_BITS = WORKER_ID_BITS + SEQUENCE_BITS;

	// 最大的机器码
	private static final int WORKER_ID_MAX_VALUE = 1 << WORKER_ID_BITS;
	// 最大序列码 用来比较
	private static final int SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

	// 机器码
	private int workerId;

	// 序列码
	private long sequence;

	// 上一次的时间
	private long lastTime;

	// 开始时间 能使ID小一点
	public static final long EPOCH;
	static {
		Calendar calendar = Calendar.getInstance();
		// Mickey写于2017年9月18日
		calendar.set(2017, Calendar.SEPTEMBER, 18);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		EPOCH = calendar.getTimeInMillis();
	}

	/**
	 * 没有机器码，默认0
	 */
	public Snowflake() {
	}

	/**
	 * @param workerId
	 *            机器码
	 */
	public Snowflake(int workerId) {
		if (workerId < 0L || workerId >= WORKER_ID_MAX_VALUE)
			new IllegalArgumentException("机器码超出范围");
		this.workerId = workerId;
	}

	/**
	 * 生成序列ID
	 * 
	 * @return 返回唯一ID
	 */
	public synchronized long generateKey() {
		long currentMillis = System.currentTimeMillis();
		if (lastTime > currentMillis)
			new IllegalArgumentException("时间错误,时间不会向后移动");

		if (lastTime == currentMillis) {
			// 比较
			if (0L == (++sequence & SEQUENCE_MASK)) {
				currentMillis = waitUntilNextTime(currentMillis);
			}
		} else {
			sequence = 0;
		}
		lastTime = currentMillis;
		return ((currentMillis - EPOCH) << WORKER_ID_SEQUENCE_BITS) | (workerId << SEQUENCE_BITS) | sequence;
	}

	/**
	 * 序列码超过之后等待下一秒
	 * 
	 * @param lastTime
	 * @return
	 */
	private long waitUntilNextTime(final long lastTime) {
		long time = System.currentTimeMillis();
		while (time <= lastTime) {
			time = System.currentTimeMillis();
		}
		return time;
	}
}
