package br.udesc.mca.trajectory.dao.column;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.model.Trajectory;

import com.datastax.driver.core.*;

public class CassandraPersistence<E> extends PersistenceDAO<E> {
    private static CassandraPersistence instance;
    private Cluster cluster;
    private Session db;
    private String space;
    private String table;
    private String qname;
    private Class type; // guarda a referencia prq recupera por reflection eh um chute



    public static CassandraPersistence getInstance() {
        return getInstance(Trajectory.class);
    }

    public static CassandraPersistence getInstance(Class type) {
        if (instance == null) {
            System.out.println(CassandraPersistence.class);
            instance = new CassandraPersistence();
            instance.type = type;
            instance.createPersistenceTable(type);
        }

        return instance;
    }

    private CassandraPersistence() {
        this.cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Metadata metadata = this.cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(),
                    host.getRack());
        }
        this.db = this.cluster.connect();
    }

    @Override
    public void createDB() {
/*
        try {
            this.db.execute("CREATE KEYSPACE dba WITH replication = {'class':'SimpleStrategy',"
                    + " 'replication_factor':3};");
            this.db.execute("create table dba.customer (id int primary key, name text);");
            this.db.execute("create table dba.customerdata (id int primary key, customerid int, key text, value text);");
            this.db.execute("create index customerid on dba.customerdata (customerid)");
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }*/
    }

    @Override
    public List<E> findAll() {
        List<E> ret = new ArrayList<>();
        try {
            ResultSet rs = this.db.execute("select * from " + qname);
            for (Row row : rs) {
                ret.add(readRow(row));
            }
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public E findById(long id) {
        E ret = null;
        try {
            PreparedStatement ps = this.db.prepare("select * from "+qname+" where id=?");
            BoundStatement bs = new BoundStatement(ps);
            bs.setLong(0, id);
            ResultSet rs = this.db.execute(bs);
            Row row = rs.one();
            if (row != null) {
                ret = readRow(row);
            }

        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(long id) {
        try {
            PreparedStatement ps = this.db.prepare("delete from " + qname + " where id=?");
            BoundStatement bs = new BoundStatement(ps);
            bs.setLong(0, id);
            this.db.execute(bs);
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        this.db.execute("drop schema "+ space);
        this.db.close();
        this.db = null;
        this.cluster.close();
        this.cluster = null;
    }

    //////////////////////////////TEstando metodos abaixo... estao longe de estar na versao final
    @Override
    public E store(E o) {
        StringBuilder sb = new StringBuilder();
        StringBuilder values = new StringBuilder();

        sb.append("insert into ").append(qname).append("(");
        try {
            boolean c = false;
            TableMetadata tb = cluster.getMetadata().getKeyspace(space).getTable(table);
            List<ColumnMetadata> cols = tb.getColumns();

            for (ColumnMetadata col : cols) {
                String getter = "get" + col.getName().substring(0, 1).toUpperCase() + col.getName().substring(1);
                if (c) {
                    sb.append(",");
                    values.append(",");
                }
                sb.append(col.getName());

                if ("TEXT".equals(col.getType().getName().name())) {
                    values.append("'").append(type.getDeclaredMethod(getter).invoke(o)).append("'");
                } else {
                    values.append(type.getDeclaredMethod(getter).invoke(o));
                }

                c = true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        sb.append(") values (").append(values.toString()).append(")");
        this.db.execute(sb.toString());

        return o;
    }

    private E readRow(Row row) throws Exception {
        Object ret = type.newInstance();
        for(ColumnDefinitions.Definition def: row.getColumnDefinitions().asList()){
            String setter = "set" + def.getName().substring(0,1).toUpperCase() + def.getName().substring(1);
            Field f = type.getDeclaredField(def.getName());

            if(Byte.class.equals(f.getType())){
                Integer v = row.getInt(f.getName());
                if(v != null) {
                    type.getDeclaredMethod(setter, Byte.class).invoke(ret, v.byteValue());
                }
            } else if(Short.class.equals(f.getType())){
                Integer v = row.getInt(f.getName());
                if(v != null) {
                    type.getDeclaredMethod(setter, Short.class).invoke(ret, v.shortValue());
                }
            } else if(Integer.class.equals(f.getType())){
                type.getDeclaredMethod(setter, Integer.class).invoke(ret, row.getInt(f.getName()));
            } else if("int".equals(f.getType().getName()) ){
                type.getDeclaredMethod(setter, Integer.TYPE).invoke(ret, row.getInt(f.getName()));
            } else if(Long.class.equals(f.getType())){
                type.getDeclaredMethod(setter, Long.class).invoke(ret, row.getLong(f.getName()));
            }  else if("long".equals(f.getType().getName())){
                type.getDeclaredMethod(setter, Long.TYPE).invoke(ret, row.getLong(f.getName()));
            } else if(Float.class.equals(f.getType())){
                type.getDeclaredMethod(setter, Float.class).invoke(ret, row.getFloat(f.getName()));
            } else if(Double.class.equals(f.getType())){
                type.getDeclaredMethod(setter, Double.class).invoke(ret, row.getDouble(f.getName()));
            } else if(String.class.equals(f.getType())){
                type.getDeclaredMethod(setter, String.class).invoke(ret, row.getString(f.getName()));
            }
        }

        return (E) ret;
    }


    /**
     * Cria a tabela baseados nos atributos de uma classe (obrigatorio a classe ter um atributo long id)... atualmente soh persiste primitivos e string
     * @param
     */
    private void createPersistenceTable(Class klass){
        String name = klass.getName();
        space = name.substring(0, name.lastIndexOf('.')).replace('.','x');
        table = klass.getName().substring(name.lastIndexOf('.') + 1);
        qname = space + "." + table;
        Metadata meta = this.cluster.getMetadata();
        KeyspaceMetadata ks = null;
        TableMetadata tb = null;
        try {
            ks = meta.getKeyspace(space);
        }catch (Exception e){}

        if(ks != null){
            try {
                tb = ks.getTable(table);
            }catch (Exception e){}
        } else {
            this.db.execute("CREATE KEYSPACE "+space+" WITH replication = {'class':'SimpleStrategy',"
                    + " 'replication_factor':3};");
        }

        if(tb == null){
            StringBuilder sb = new StringBuilder();
            sb.append("create table ").append(qname).append(" (id bigint primary key");
            Field[] fs = klass.getDeclaredFields();

            for(Field f:fs){
                if(Modifier.isStatic(f.getModifiers()) || (! f.getType().isPrimitive() && ! String.class.equals(f.getType()))){//desconsiderar staticos e nao primitivos por enquanto
                    continue;
                }
                switch (f.getName()){
                    case "id":
                    case "serialversion":
                        //ignorados
                        break;
                    default:

                        if(Byte.class.equals(f.getType())){
                            sb.append(",").append(f.getName()).append(" int ");
                        } else if(Short.class.equals(f.getType())){
                            sb.append(",").append(f.getName()).append(" int ");
                        } else if(Integer.class.equals(f.getType())){
                            sb.append(",").append(f.getName()).append(" int ");
                        } else if(Long.class.equals(f.getType())){
                            sb.append(",").append(f.getName()).append(" bigint ");
                        } else if(Float.class.equals(f.getType())){
                            sb.append(",").append(f.getName()).append(" float ");
                        } else if(Double.class.equals(f.getType())){
                            sb.append(",").append(f.getName()).append(" double ");
                        } else if(String.class.equals(f.getType())){
                            sb.append(",").append(f.getName()).append(" text ");
                        }

                }
            }
            sb.append(")");
            this.db.execute(sb.toString());
            //this.db.execute("create index "+table+"id on "+qname+"(id)");  PK jah eh indice
        }
    }
}
