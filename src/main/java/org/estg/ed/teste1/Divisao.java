package org.estg.ed.teste1;
// DivisaoMock.java (ou Divisao.java)
public class Divisao {
    private String name;
    private boolean isCentral;

    public Divisao(String name) {
        this.name = name;
        this.isCentral = isCentral;
    }

    public String getName() { return name; }
    public boolean isCentralRoom() { return isCentral; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Divisao divisao = (Divisao) o;
        return name.equals(divisao.name); // Implementação simplificada
    }
    // Adicione um hashCode() simples se usar equals
}