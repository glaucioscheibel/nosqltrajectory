package br.udesc.mca.sec1.projeto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private List<CustomerData> customerData;

    public Customer() {}

    public Customer(Integer id) {
        this.id = id;
    }

    public Customer(Integer id, String nome, String... data) {
        this(id);
        this.name = nome;
        for (int i = 0, n = data.length; i < n; i += 2) {
            this.addCustomerData(data[i], data[i + 1]);
        }
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CustomerData> getCustomerData() {
        if (this.customerData == null) {
            this.customerData = new ArrayList<>();
        }
        return Collections.unmodifiableList(this.customerData);
    }

    public void setCustomerData(List<CustomerData> customerData) {
        this.customerData = customerData;
    }

    public void addCustomerData(String key, String value) {
        CustomerData cd = new CustomerData(key, value);
        if (this.customerData == null) {
            this.customerData = new ArrayList<>();
        }
        this.customerData.add(cd);
    }

    @Override
    public int hashCode() {
        if (this.id == null) {
            return 0;
        }
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + this.id + ", name=" + this.name + '}';
    }
}
