<<<<<<< HEAD
package busca;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * Busca a solu��o por busca em largura.
 *
 *  @author Jomi Fred H�bner
 */
public class BuscaLargura extends Busca {
    
    /** busca sem mostrar status */
    public BuscaLargura() {
    }
    
    /** busca mostrando status */
    public BuscaLargura(MostraStatusConsole ms) {
        super(ms);
    }

    public Nodo busca(Estado inicial) {
        status.inicia();
        initFechados();
       
        Queue<Nodo> abertos = new PriorityQueue<Nodo>();
        
        abertos.add(new Nodo(inicial, null));
        
        while (!parar && abertos.size() > 0) {
            
            //System.out.print("abertos "+abertos);
            Nodo n = abertos.remove();
            //System.out.println("pegando "+n);
            status.explorando(n, abertos.size());
            if (n.estado.ehMeta()) {
                status.termina(true);
                return n;
            }
                        
            abertos.addAll( sucessores(n) );
        }
        status.termina(false);
        return null;
    }
    
    public String toString() {
    	return "BL - Busca em Largura";
    }
}
=======
package busca;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * Busca a solu��o por busca em largura.
 *
 *  @author Jomi Fred H�bner
 */
public class BuscaLargura extends Busca {
    
    /** busca sem mostrar status */
    public BuscaLargura() {
    }
    
    /** busca mostrando status */
    public BuscaLargura(MostraStatusConsole ms) {
        super(ms);
    }

    public Nodo busca(Estado inicial) {
        status.inicia();
        initFechados();
       
        Queue<Nodo> abertos = new PriorityQueue<Nodo>();
        
        abertos.add(new Nodo(inicial, null));
        
        while (!parar && abertos.size() > 0) {
            
            //System.out.print("abertos "+abertos);
            Nodo n = abertos.remove();
            //System.out.println("pegando "+n);
            status.explorando(n, abertos.size());
            if (n.estado.ehMeta()) {
                status.termina(true);
                return n;
            }
                        
            abertos.addAll( sucessores(n) );
        }
        status.termina(false);
        return null;
    }
    
    public String toString() {
    	return "BL - Busca em Largura";
    }
}
>>>>>>> 022ab491887dc7d4e34ef852b1005a90beb3c231
