/**
* Universidad Del Valle de Guatemala
* 12-feb-2016
* Pablo Díaz 13203
*/

package compiler;

import antlr4.programBaseVisitor;
import antlr4.programParser;


/**
 *
 * @author Pablo
 * @param <T>
 */
public class Visitor<T> extends programBaseVisitor {
    
    public int ambitoActual=0;
   
    private final SymbolTable tablaSimbolos;
    public int autoincrement = 0;
    public Scope scopeActual;
    public static boolean verificacion = true;
    
   
    
    
    
    

    public Visitor() {
       
        scopeActual = new Scope();
        scopeActual.setIdScope(ambitoActual);
        this.tablaSimbolos = new SymbolTable();
       
       
    }
    @Override 
    public T visitProgram(programParser.ProgramContext ctx) {
        System.out.println("YOLO: " + ctx.getText());
        System.out.println(ctx.getChildCount());
        
        for (int i = 0;i<ctx.getChildCount();i++){
           System.out.println(ctx.getChild(i).getText());
           this.visit(ctx.getChild(i));
        }
       
       
        
        tablaSimbolos.printSymbolTable();
        return (T)"";
    }
    
    
    @Override
    public T visitDeclaration(programParser.DeclarationContext ctx){
        System.out.println("cantidad declaration: " + ctx.getChildCount());
        for (int i = 0;i<ctx.getChildCount();i++){
            this.tablaSimbolos.addSymbol((Symbol)this.visit(ctx.getChild(i)));
        }
        
        
        return (T)"void";
    }
    /**
     * Debe devolver un símbolo
     * @param ctx
     * @return 
     */
    @Override
    public T visitStructDeclaration(programParser.StructDeclarationContext ctx){
        
        StructType struct = new StructType();
        
        Scope scopeStruct = new Scope();
        
        scopeActual.addSiguiente(scopeStruct);
        scopeStruct.setAnterior(scopeActual);
        scopeActual = scopeStruct;
        
       
        struct.setLiteralTipo(ctx.getChild(0).getText());
        struct.setNombreVariable(ctx.getChild(1).getText());
        for (int i = 0;i<ctx.getChildCount();i++){
            Symbol simbolo = (Symbol)visit(ctx.getChild(i));
            if (simbolo != null){
                this.tablaSimbolos.addSymbol(simbolo);
                struct.addMember(simbolo);
            }
        }
        
        
        scopeActual = scopeActual.getAnterior();
        Symbol simbolo = new Symbol(++autoincrement, scopeActual.getIdScope(),struct);
        
        return (T)simbolo ;
    }
    
   
    
    
    
    
    
    @Override
    public T visitVarDeclarationID(programParser.VarDeclarationIDContext ctx){
        System.out.println(ctx.getChildCount()+"cantidad var declaration");
        
        
        //param1 = nombre variable
        //param2 = nombre tipo
        Type tipo = new Type(ctx.getChild(1).getText(),ctx.getChild(0).getText());
        Symbol simbolo = new Symbol(++autoincrement,scopeActual.getIdScope(),tipo);
       
        
        return (T)simbolo;
    }

    @Override
    public T visitVarDeclarationArray(programParser.VarDeclarationArrayContext ctx) {
        
        Type tipo = new Type(ctx.getChild(1).getText(),ctx.getChild(0).getText());
        tipo.setArreglo(Integer.parseInt(ctx.getChild(3).getText()));
        
        Symbol simbolo = new Symbol(++autoincrement,scopeActual.getIdScope(),tipo);
       
        
        return (T)simbolo;
    }

    @Override
    public Object visitMethodDeclaration(programParser.MethodDeclarationContext ctx) {
    
         MethodType tipo = new MethodType();
        
        Scope scopeMethod = new Scope();
        
        scopeActual.addSiguiente(scopeMethod);
        scopeMethod.setAnterior(scopeActual);
        scopeActual = scopeMethod;
        
       
        tipo.setLiteralTipo(ctx.getChild(0).getText());
        tipo.setNombreVariable(ctx.getChild(1).getText());
        for (int i = 0;i<ctx.getChildCount();i++){
            Symbol simbolo = (Symbol)visit(ctx.getChild(i));
            if (simbolo != null){
                if (simbolo.getTipo().isParametro())
                    tipo.addParameter(simbolo);
                this.tablaSimbolos.addSymbol(simbolo);
                    
                
            }
        }
        
        
        scopeActual = scopeActual.getAnterior();
        Symbol simbolo = new Symbol(++autoincrement, scopeActual.getIdScope(),tipo);
        
        return (T)simbolo;
    }

    @Override
    public T visitBlock(programParser.BlockContext ctx) {
      for (int i = 0;i<ctx.getChildCount();i++){
           Symbol simbolo = (Symbol)visit(ctx.getChild(i));
            if (simbolo != null){
                 
                this.tablaSimbolos.addSymbol(simbolo);
                
            }
      }
   
      return (T)null;
    }
    
    @Override
    public T visitParameterID(programParser.ParameterIDContext ctx) {
        System.out.println("ENTRA");
        Type tipo = new Type(ctx.getChild(1).getText(),ctx.getChild(0).getText());
        Symbol simbolo = new Symbol(++autoincrement,this.scopeActual.getIdScope(),tipo);
        tipo.setParametro();
        
        return (T)simbolo;
    }

 

    
    @Override
    public T visitVarType(programParser.VarTypeContext ctx){
        
        
        return (T)ctx.getText();
        
    }

    @Override
    public T visitStatementIF(programParser.StatementIFContext ctx) {
    
        Scope scopeIF = new Scope();
        
        scopeActual.addSiguiente(scopeIF);
        scopeIF.setAnterior(scopeActual);
        scopeActual = scopeIF;
        
        for (int i = 0;i<ctx.getChildCount();i++){
            this.visit(ctx.getChild(i));
        }
         scopeActual = scopeActual.getAnterior();
         
       
        return (T)null;
    }

    @Override
    public T visitStatementWhile(programParser.StatementWhileContext ctx) {
       
        
        Scope scopeWhile = new Scope();
        
        scopeActual.addSiguiente(scopeWhile);
        scopeWhile.setAnterior(scopeActual);
        scopeActual = scopeWhile;
        
        for (int i = 0;i<ctx.getChildCount();i++){
            this.visit(ctx.getChild(i));
        }
         scopeActual = scopeActual.getAnterior();
         
      
        return (T)null;
    }

    @Override
    public T visitStatementElse(programParser.StatementElseContext ctx) {
         Scope scopeElse = new Scope();
        
        scopeActual.addSiguiente(scopeElse);
        scopeElse.setAnterior(scopeActual);
        scopeActual = scopeElse;
        
        for (int i = 0;i<ctx.getChildCount();i++){
            this.visit(ctx.getChild(i));
        }
         scopeActual = scopeActual.getAnterior();
         
      
        return (T)null;
    }

    @Override
    public T visitLocation(programParser.LocationContext ctx) {
        return (T)ctx.getChild(0).getText(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitStatementLocation(programParser.StatementLocationContext ctx) {
        
        //aqui tengo el nombre de la variable
        String nombreVar = (String)this.visit(ctx.getChild(0));
        
        //ahora verifico que ya esté declarada.
        boolean encontrado = this.tablaSimbolos.revisarNombreVar(nombreVar, scopeActual);
        System.out.println("");
        if (!encontrado){
            System.out.println("Variable no declarada: línea " + ctx.getStart().getLine()+
                    " columna: " + ctx.getStart().getCharPositionInLine());
        }
        System.out.println("");
        if (encontrado){
            Symbol simboloEncontrado = this.tablaSimbolos.showSymbol(nombreVar, scopeActual);

            String tipo =   simboloEncontrado.getTipo().getLiteralTipo();
            String tipoDeclarado =  (String)this.visit(ctx.getChild(2));
                System.out.println(tipo);
                System.out.println(tipoDeclarado);
            if (tipoDeclarado.contains("literal")){
                if (tipoDeclarado.contains(tipo))
                        System.out.println("Tipo Correcto!");
                else{
                        System.out.println("Tipo incorrecto");
                        Visitor.verificacion = false;
                }
            }
            else{
                String search = buscarRecursivo(tipoDeclarado,tipo);
                if (search.contains(tipo)){
                    System.out.println("Tipo Correcto");
                }
                else{
                    System.out.println("Tipo Incorrecto");
                }
                
            }
        }
        
        //ahora reviso que tenga el tipo correcto.
        
        
        return null; //To change body of generated methods, choose Tools | Templates.
    }

    public String buscarRecursivo(String nombreVar,String tipo){
         boolean encontrado = this.tablaSimbolos.revisarNombreVar(nombreVar, scopeActual);
        System.out.println("");
        if (!encontrado){
            /*System.out.println("Variable no declarada: línea " + ctx.getStart().getLine()+
                    " columna: " + ctx.getStart().getCharPositionInLine());*/
        }
        System.out.println("");
        if (encontrado){
            Symbol simboloEncontrado = this.tablaSimbolos.showSymbol(nombreVar, scopeActual);
            return simboloEncontrado.getTipo().getLiteralTipo();
           
              
         }
        
        return null;
    }
    
    @Override
    public Object visitInt_literal(programParser.Int_literalContext ctx) {
        return ANTGui.ruleNames[ctx.getRuleContext().getRuleIndex()];
        
    }

    @Override
    public Object visitChar_literal(programParser.Char_literalContext ctx) {
       return ANTGui.ruleNames[ctx.getRuleContext().getRuleIndex()];
    }

    @Override
    public Object visitBool_literal(programParser.Bool_literalContext ctx) {
        return ANTGui.ruleNames[ctx.getRuleContext().getRuleIndex()];
    }
    
    
    
}