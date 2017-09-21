package pro.mickey.snowflake;

import org.hibernate.HibernateException;

/**
 * 用于整合spring data 和 hibernate
 * 
 * @author Mickey
 */
public class SnowflakeMybatis {

	private static Snowflake snowflake;

	public static long generate(int mid) throws HibernateException {
		if (snowflake == null)
			snowflake = new Snowflake(mid);
		return snowflake.generateKey();
	}

}