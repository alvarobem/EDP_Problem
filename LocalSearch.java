/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edp;

/**
 *
 * @author atg
 */
public interface LocalSearch {
    public Solution improvementSolution (Solution sol, int rep, Builder builder, int numDesc);
}
