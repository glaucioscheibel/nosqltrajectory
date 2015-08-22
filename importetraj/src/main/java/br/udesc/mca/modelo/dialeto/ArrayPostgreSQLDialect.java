package br.udesc.mca.modelo.dialeto;

import java.sql.Types;

import org.hibernate.spatial.dialect.postgis.PostgisDialect;

public class ArrayPostgreSQLDialect extends PostgisDialect {

	private static final long serialVersionUID = 1L;

	public ArrayPostgreSQLDialect() {
		super();

		/**
		 * For other type array you can change String[] to that array type
		 */

		this.registerColumnType(Types.ARRAY, "varchar[]");
	}

}
