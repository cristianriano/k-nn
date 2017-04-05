/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vecinos;

import entidad.Paciente;
import entidad.PacienteNormalizado;
import java.util.ArrayList;
import java.util.Collection;
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
public class CrossValidation {
    
    public List<Paciente> todos;
    public Knn knn;
    public int ids = 0;
    
    public CrossValidation(){
        todos = listarTodos();
        //contarRegistros();
        ids = 200;
    }
    
    public int contarRegistros(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("K-NNPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int x = (int) em.createQuery("SELECT count(p.id) FROM Paciente p").getSingleResult();
        tx.commit();
        em.close();
        emf.close();
        return x;
    }
    
    public List<Paciente> listarTodos(){
        List<Paciente> lista;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("K-NNPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Query query = em.createNamedQuery("Paciente.findAll");
        lista = query.getResultList();
        tx.commit();
        em.close();
        emf.close();
        //Collections.shuffle(lista);
        return lista;
    }
    
    public int calcularMejorK(int maxK){
        
        int[] aciertos = new int[maxK];
        List <Paciente> training;
        
        for(int x=1; x<=maxK; x++){
            for(int i=0; i<ids; i=i+ids/10){
                training = new ArrayList<Paciente>();
                training.addAll(todos.subList(0, i));
                training.addAll(todos.subList(i+ids/10, ids));
                
                knn = new Knn(training);
                
                for(int z=0; z<ids/10; z++){
                    knn.setK(x);
                    knn.setPaciente(todos.get(z+i));
                    
                    if(knn.calcularDroga().equals(todos.get(z+i).getDrug())) aciertos[x-1] = aciertos[x-1] + 1;
                    System.out.println(knn.calcularDroga());
                    System.out.println(todos.get(z+i).getDrug());
                    System.out.println(aciertos[0]);
                    System.out.println("-------------------------------------");
                }
                System.out.println(aciertos[0]);
            }
        }
        int max = 0, mejorK = 0;
        double porcentaje;
        for(int x=0; x<maxK; x++){
            porcentaje = aciertos[x]/200.0;
            //System.out.println(aciertos[x]);
            if(aciertos[x]>max){
                max = aciertos[x];
                mejorK = x+1;
            }
        }
        return mejorK;
    }
    
    
}
