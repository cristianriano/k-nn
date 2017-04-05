/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Cristian
 */
@Entity
@Table(name = "paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p"),
    //@NamedQuery(name = "Paciente.findAllMix", query = "SELECT p FROM Paciente p ORDER BY rand()"),
    @NamedQuery(name = "Paciente.findByAge", query = "SELECT p FROM Paciente p WHERE p.age = :age"),
    @NamedQuery(name = "Paciente.findByNa", query = "SELECT p FROM Paciente p WHERE p.na = :na"),
    @NamedQuery(name = "Paciente.findByK", query = "SELECT p FROM Paciente p WHERE p.k = :k"),
    @NamedQuery(name = "Paciente.findById", query = "SELECT p FROM Paciente p WHERE p.id = :id")})
public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Age")
    private Double age;
    @Lob
    @Column(name = "Sex")
    private String sex;
    @Lob
    @Column(name = "BP")
    private String bp;
    @Lob
    @Column(name = "Cholesterol")
    private String cholesterol;
    @Column(name = "Na")
    private Double na;
    @Column(name = "K")
    private Double k;
    @Lob
    @Column(name = "Drug")
    private String drug;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;

    public Paciente() {
        id = 0;
    }
    
    public Paciente(double a, String s, String b, String c, double n, double ka, String d){
        age = a;
        k = ka;
        na = n;
        sex = s;
        bp = b;
        cholesterol = c;
        drug = d;
        id = 0;
    }

    public Paciente(Integer id) {
        this.id = id;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getNa() {
        return na;
    }

    public void setNa(Double na) {
        this.na = na;
    }

    public Double getK() {
        return k;
    }

    public void setK(Double k) {
        this.k = k;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Paciente[ id=" + id + " ]";
    }
    
}
