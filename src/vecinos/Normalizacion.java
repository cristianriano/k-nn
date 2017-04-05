/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vecinos;

import entidad.Paciente;
import entidad.PacienteNormalizado;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristian
 */
public class Normalizacion {
    
    // Se convierten todos los datos a numericos
    public List<PacienteNormalizado> convertirDatos(List<Paciente> pacientes){
        List<PacienteNormalizado> pacientesNorm = new ArrayList<PacienteNormalizado>();
        
        for(Paciente p : pacientes){
            pacientesNorm.add(convertirPaciente(p));
        }
        return pacientesNorm;
    }
    
    public PacienteNormalizado convertirPaciente(Paciente p){
        PacienteNormalizado pn = new PacienteNormalizado();
        
        //Sex: M = 0 & F = 1
        if(p.getSex().equals("M")) pn.setSex(0.0);
        else pn.setSex(1.0);
            
        //Cholesterol: HIGH = 1 & NORMAL = 0
        if(p.getCholesterol().equals("HIGH")) pn.setCholesterol(1.0);
        else pn.setCholesterol(0.0);
            
        //Bp: HIGH = 1 & NORMAL = 0.5 & LOW = 0
        if(p.getBp().equals("HIGH")) pn.setBp(1.0);
        else if(p.getBp().equals("NORMAL")) pn.setBp(0.5);
        else pn.setBp(0.0);
        
        pn.setAge(p.getAge());
        pn.setK(p.getK());
        pn.setNa(p.getNa());
        pn.setDrug(p.getDrug());
        pn.setId(p.getId());
            
        return pn;
    }
    
    public List<PacienteNormalizado> normalizarDatos(List<PacienteNormalizado> pacientes, double maxAge, double maxNa, double maxK){
        for(PacienteNormalizado p : pacientes){
            p = normalizarPaicente(p, maxAge, maxNa, maxK);
        }
        return pacientes;
    }
    
    public PacienteNormalizado normalizarPaicente(PacienteNormalizado p, double maxAge, double maxNa, double maxK){
        p.setAge(p.getAge()/maxAge);
        p.setNa(p.getNa()/maxNa);
        p.setK(p.getK()/maxK);
        return p;
    }
    
    public double distancia(PacienteNormalizado p1, PacienteNormalizado p2){
        double difBp;
        if (p1.getBp().equals(p2.getBp())) difBp = 0.0;
        else difBp = 1.0;
        return (Math.sqrt(Math.pow(p1.getAge() - p2.getAge(), 2)+Math.pow(difBp, 2)+
                Math.pow(p1.getCholesterol() - p2.getCholesterol(), 2)+Math.pow(p1.getK() - p2.getK(), 2)+
                Math.pow(p1.getNa() - p2.getNa(), 2) + Math.pow(p1.getSex() - p2.getSex(), 2)));
    }
    
}
