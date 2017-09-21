package pro.mickey.snowflake;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 用于整合spring data 和 hibernate
 * 
 * @author Mickey
 */
public class SnowflakeHibernate implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return new Snowflake().generateKey();
	}

}