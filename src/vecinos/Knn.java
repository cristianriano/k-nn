/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vecinos;

import entidad.Paciente;
import entidad.PacienteNormalizado;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Cristian
 */
public class Knn {
    
    private List<PacienteNormalizado> pacientesNorm;
    private double maxAge=0, maxNa=0, maxK=0;
    private Normalizacion norm;
    private PacienteNormalizado[] vecinos;
    private Double[] distancias;
    private int k;
    private Paciente paciente;
    
    public Knn(){
        norm = new Normalizacion();
        pacientesNorm = norm.convertirDatos(listarTodos());
        calcularMaximos();
        normalizar();
    }
    
    public Knn(List<Paciente> training){
        norm = new Normalizacion();
        pacientesNorm = norm.convertirDatos(training);
        calcularMaximos();
        normalizar();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public List<PacienteNormalizado> getPacientesNorm(){
        return pacientesNorm;
    }
    
    public List<Paciente> listarTodos(){
        List<Paciente> pacientes;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("K-NNPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query query = em.createNamedQuery("Paciente.findAll");
        pacientes = query.getResultList();
        tx.commit();
        em.close();
        emf.close();
        return pacientes;
    }
    
    public void calcularMaximos(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("K-NNPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        maxAge = (Double) em.createQuery("SELECT max(p.age) FROM Paciente p").getSingleResult();
        maxNa = (Double) em.createQuery("SELECT max(p.na) FROM Paciente p").getSingleResult();
        maxK = (Double) em.createQuery("SELECT max(p.k) FROM Paciente p").getSingleResult();
        tx.commit();
        em.close();
        emf.close();
        
        /*
        for(PacienteNormalizado p : pacientesNorm){
            if(p.getAge()>maxAge) maxAge = p.getAge();
            if(p.getNa()>maxNa) maxNa = p.getNa();
            if(p.getK()>maxK) maxK = p.getK();
        }
        */
    }
    
    public void normalizar(){
        pacientesNorm = norm.normalizarDatos(pacientesNorm, maxAge, maxNa, maxK);
    }
    
    public void calcularVecinos(){
        PacienteNormalizado pn = norm.normalizarPaicente(norm.convertirPaciente(paciente), maxAge, maxNa, maxK);
        vecinos = new PacienteNormalizado[k];
        distancias = new Double[k];
        double dist = 0, distancia = 0;
        int index = 0;
        
        for(int x=0; x<pacientesNorm.size(); x++){
            if(x<k){
                vecinos[x] = pacientesNorm.get(x);
                distancias[x] = norm.distancia(pn, pacientesNorm.get(x));
                if(distancias[x]>dist){
                    index = x;
                    dist = distancias[x];    
                }
            }
            else{
                /*for(int z=0; z<k; z++){
                    System.out.println(distancias[z]);
                }
                System.out.println("----------------------------------");*/
                distancia = norm.distancia(pn, pacientesNorm.get(x));
                if(distancia<dist){
                    distancias[index] = distancia;
                    vecinos[index] = pacientesNorm.get(x);
                    index=0;
                    dist=distancias[0];
                    for(int y=1; y<k; y++){
                        if(distancias[y]>dist){
                        index = y;
                        dist = distancias[y];    
                        }
                    }
                }
            }
        }
    }
    
    public String calcularDroga(){
        calcularVecinos();
        double invA = 0 , invB = 0, invC = 0, invX = 0, invY = 0;
        List inversos = new ArrayList();
       
        for(int x=0; x<k; x++){
            if(vecinos[x].getDrug().equals("drugA")) invA = invA + (Math.pow(1/distancias[x], 2));
            else if(vecinos[x].getDrug().equals("drugB")) invB = invB + (Math.pow(1/distancias[x], 2));
            else if(vecinos[x].getDrug().equals("drugC")) invC = invC + (Math.pow(1/distancias[x], 2));
            else if(vecinos[x].getDrug().equals("drugX")) invX = invX + (Math.pow(1/distancias[x], 2));
            else invY = invY + (Math.pow(1/distancias[x], 2));
        }
        /*
        inversos.add(invA);
        inversos.add(invB);
        inversos.add(invC);
        inversos.add(invX);
        inversos.add(invY);
        
        Collections.sort(inversos);
        
        if(inversos.get(4).equals(invA)) return "drugA";
        if(inversos.get(4).equals(invB)) return "drugB";
        if(inversos.get(4).equals(invC)) return "drugC";
        if(inversos.get(4).equals(invX)) return "drugX";
        return "drugY";
       */
       if(invA > invB && invA > invC && invA > invX && invA > invY) return "drugA";
       else if(invB > invA && invB > invC && invB > invX && invB > invY) return "drugB";
       else if(invC > invA && invC > invB && invC > invX && invC > invY) return "drugC";
       else if(invX > invA && invX > invC && invX > invB && invX > invY) return "drugX";
       else if(invY > invA && invY > invC && invY > invB && invY > invX) return "drugY";
       else return "HPTA";
    }
}

