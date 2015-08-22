package br.udesc.mca.modelo.tipo;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

//http://www.vivekpatidar.com/?p=15
//http://stackoverflow.com/questions/21940642/hibernate-postgres-array-type
//https://finethinking.wordpress.com/2011/10/27/hibernate-arrays-and-postgres/
//http://blog.xebia.com/2009/11/09/understanding-and-writing-hibernate-user-types/
//http://i-proving.com/2005/08/03/user-types-in-hibernate/
//https://forum.hibernate.org/viewtopic.php?f=1&t=962047
//http://www.java2s.com/Questions_And_Answers/JPA/Map/array.htm
//https://forum.hibernate.org/viewtopic.php?f=1&t=946973&start=15
//https://forum.hibernate.org/viewtopic.php?f=1&t=962047&start=0&sid=8b889700151b9ef8d143d00e8b0a3d4d
//https://forum.hibernate.org/viewtopic.php?f=1&t=962047&start=0
public class StringArrayUserType implements UserType {

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.ARRAY };
	}

	@Override
	public Class<String[]> returnedClass() {
		return String[].class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == null) {
			return y == null;
		}
		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		final String resultRaw = rs.getString(names[0]);

		if (rs.wasNull()) {
			return null;
		} else {
			Pattern pat = Pattern.compile("[{}]");
			Matcher matcher = pat.matcher(resultRaw);
			String intermediate = matcher.replaceAll("");

			pat = Pattern.compile(",");
			return (pat.split(intermediate));
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		Connection connection = st.getConnection();
		String[] castObject = (String[]) value;
		Array array = connection.createArrayOf("varchar", castObject);
		st.setArray(index, array);

	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (String[]) this.deepCopy(value);
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return this.deepCopy(cached);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}