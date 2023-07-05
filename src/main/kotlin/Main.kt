import java.lang.Exception
import kotlin.math.absoluteValue

fun main(args: Array<String>) {

    var ticTacToe = TicTacToe() //Instancio el juego

    ticTacToe.cargarCadena("_________")// Ingreso la posición inicial del juego (todas as casillas en blanco)
    ticTacToe.MostrarTablero()


    while (true) {

        var fila: Int = 100
        var columna: Int = 100

        while (true) { //Valido la entrada de coordenadas t
            //todo esta validacion es muy mejorable
            val input = readLine()
            try {
                val tokens = input?.split(" ")
                if (input != null) {
                    if (contieneOtrosCaracteres(input!!)) {
                        println("You should enter numbers!")

                        throw IllegalArgumentException()

                    }
                }

                if (tokens?.size != 2) {
                    throw IllegalArgumentException()
                }

                var filaTemp = tokens[0].toIntOrNull()
                if (filaTemp != null) {
                    fila = filaTemp
                }
                else {
                    println("You should enter numbers!")
                    throw IllegalArgumentException()
                }

                var columnaTemp = tokens[1].toIntOrNull()
                if (columnaTemp != null) {
                    columna = columnaTemp
                }
                else {
                    println("You should enter numbers!")
                    throw IllegalArgumentException()
                }

                if (!(fila in 1..3)) {
                    println("Coordinates should be from 1 to 3!")
                    throw IllegalArgumentException()
                }
                if (!(columna in 1..3)) {
                    println("Coordinates should be from 1 to 3!")
                    throw IllegalArgumentException()
                }

                if (ticTacToe.Ocupada(fila - 1, columna - 1)) {
                    println("This cell is occupied! Choose another one!")
                    throw IllegalArgumentException()
                }

                break // Salir del bucle si no se detectó ningún error, las coordenadas son válidas
            } catch (e: Exception) {
                // println("Las coordenadas o comando ingresado no válidos. Inténtelo nuevamente.")
            }


        }
        fila--
        columna--

        ticTacToe.Jugar(fila, columna, ticTacToe.Turno())
        ticTacToe.MostrarTablero()


        when (ticTacToe.GameState()) {
            "Impossible" -> {// como está hecho el program nunca tendría que pasa por aquí
                println("Impossible")
                break
            }

            "X wins" -> {
                println("X wins")
                break
            }

            "O wins" -> {
                println("O wins")
                break
            }

            "Draw" -> {
                println("Draws")
                break
            }

            "Game not finished" -> {// no hace falta hacer nada, se sige dentro del bucle
            }
        }
    }
}


fun contieneOtrosCaracteres(cadena: String): Boolean {
    for (caracter in cadena) {
        if (!caracter.isDigit() && caracter != ' ') {
            return true // La cadena contiene caracteres que no son dígitos ni espacio
        }
    }
    return false // La cadena solo contiene dígitos
}


/**
 *
 *                                    Clase TicTacToe
 *
 */
class TicTacToe {
    val matriz: Array<Array<Char>> = Array(3) { Array(3) { '_' } }

    fun MostrarTablero() {
        println("---------")
        for (f in 0..2) {
            println("| ${matriz[f][0]} ${matriz[f][1]} ${matriz[f][2]} |")
        }
        println("---------")
    }


    fun cargarCadena(cadena: String) {
        for (f in 0..2) {
            for (c in 0..2) {
                matriz[f][c] = (cadena.substring(f * 3 + c, f * 3 + c + 1))[0]

            }
        }
    }


    fun GameState(): String {

        //chequear primero por imposible

        if (Impossible()) {
            return "Impossible"
        }

        if (TresEnRalla('X')) {
            return "X wins"
        }
        if (TresEnRalla('O')) {
            return "O wins"
        }
        if ((CantJugadas('X') + CantJugadas('O')) > 8) {
            return "Draw"
        }
        return "Game not finished"
    }


    fun Impossible(): Boolean {
        //when the grid has three X’s in a row as well as three O’s in a row
        if (TresEnRalla('O') && TresEnRalla('X')) {
            return true
        }
        //or there are a lot more X's than O's or vice versa (the difference should be 1 or 0;
        if ((CantJugadas('X') - CantJugadas('O')).absoluteValue > 1) {
            return true
        }
        return false
    }

    fun CantJugadas(token: Char): Int {
        var contadorX = 0
        var contadorO = 0

        for (fila in matriz) {
            for (caracter in fila) {
                when (caracter) {
                    'X' -> contadorX++
                    'O' -> contadorO++
                }
            }
        }
        if (token == 'X') return contadorX
        if (token == 'O') return contadorO
        throw IllegalArgumentException("Parámetro no soportado")


    }


    fun TresEnRalla(token: Char): Boolean {
        var tresEnRallaEnFilas: Boolean = false
        var tresEnRallaEnColumnas: Boolean = false
        var tresEnRallaDiagonales: Boolean = false
        //primero recorro todas las filas para ver si hay tres en ralla
        for (f in 0..2) {
            tresEnRallaEnFilas = true
            for (c in 0..2) {
                if (matriz[f][c].toChar() != token) {
                    tresEnRallaEnFilas = false
                }
            }
            if (tresEnRallaEnFilas) {
                break
            }
        }

        //Ahora recorro todas las columnas para ver si hay tres en ralla
        for (c in 0..2) {
            tresEnRallaEnColumnas = true
            for (f in 0..2) {
                if (matriz[f][c] != token) {

                    tresEnRallaEnColumnas = false
                }
            }
            if (tresEnRallaEnColumnas) {
                break
            }
        }

        //Hora me fijo si hay tres en ralla en las diagonales
        var diag1 = true
        var diag2 = true
        for (f in 0..2) {
            if (matriz[f][f] != token) {
                diag1 = false
            }
            if (matriz[f][2 - f] != token) {
                diag2 = false
            }

        }
        tresEnRallaDiagonales = diag1 || diag2
        return tresEnRallaEnFilas || tresEnRallaEnColumnas || tresEnRallaDiagonales
    }


    fun Ocupada(fila: Int, col: Int): Boolean = matriz[fila][col] != '_'


    var turno = false
    fun Turno(): Char {
        turno = !turno
        if (turno) {
            return 'X'
        }
        else {
            return 'O'
        }
    }


    fun Jugar(fila: Int, columna: Int, turno: Char) {
        matriz[fila][columna] = turno
    }

}















