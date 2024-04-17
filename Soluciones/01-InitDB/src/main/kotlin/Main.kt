package dev.joseluisgs

import java.sql.DriverManager
import java.time.LocalDate

data class Alumno(
    val id: Long = -1,
    val nombre: String,
    val calificacion: Double,
    val aprobado: Boolean,
    val fechaNacimiento: LocalDate
)

fun main() {
    // Configurar la conexion jdbc a la base de datos
    val databaseUrl = "jdbc:sqlite:sample.db"
    val databaseUrlMemory = "jdbc:sqlite::memory:"
    val connection = DriverManager.getConnection(databaseUrl) // Abrimos la conexión
    if (connection != null) {
        println("Conexión establecida")
    } else {
        println("Error en la conexión")
    }

    // Crear la tabla
    val createTableSQL = """
        CREATE TABLE IF NOT EXISTS alumnado (
            id integer PRIMARY KEY,
            nombre text NOT NULL,
            calificacion real,
            aprobado integer,
            fecha_nacimiento text
        );
    """.trimIndent()

    val statement = connection.createStatement()
    statement.execute(createTableSQL)

    // Borrar todo
    val deleteSQL = "DELETE FROM alumnado"
    statement.execute(deleteSQL)

    // Insertar datos
    val insertSQL = """
        INSERT INTO alumnado (nombre, calificacion, aprobado, fecha_nacimiento)
        VALUES
        ('José Luis', 10.0, 1, '1980-01-01'),
        ('María', 9.0, 1, '1981-01-01'),
        ('Juan', 8, 1, '1982-01-01'),
        ('Ana', 3.75, false, '1983-01-01'),
        ('Pedro', 2, 0, '1984-01-01');
    """.trimIndent()

    val res = statement.executeUpdate(insertSQL)
    println("Filas afectadas: $res")

    // Consultar datos
    val selectSQL = "SELECT * FROM alumnado"
    val resultSet = statement.executeQuery(selectSQL)
    val alumnos = mutableListOf<Alumno>()
    while (resultSet.next()) {
        val id = resultSet.getLong("id")
        val nombre = resultSet.getString("nombre")
        val calificacion = resultSet.getDouble("calificacion")
        val aprobado = resultSet.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSet.getString("fecha_nacimiento"))
        alumnos.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }

    alumnos.forEach { println(it) }

    // Vamos a insertar un alumno
    val alumno = Alumno(
        nombre = "Javi",
        calificacion = 4.0,
        aprobado = false,
        fechaNacimiento = LocalDate.now()
    )

    val insertAlumnoSQL = """
        INSERT INTO alumnado (nombre, calificacion, aprobado, fecha_nacimiento)
        VALUES
        ('${alumno.nombre}', ${alumno.calificacion}, ${alumno.aprobado}, '${alumno.fechaNacimiento}');
    """.trimIndent()

    val resInsert = statement.executeUpdate(insertAlumnoSQL)
    println("Filas afectadas: $resInsert")

    // Consultar datos de los alumnos que se llaman Javi
    val selectJaviSQL = "SELECT * FROM alumnado WHERE nombre = 'Javi'"
    val resultSetJavi = statement.executeQuery(selectJaviSQL)
    val alumnosJavi = mutableListOf<Alumno>()
    while (resultSetJavi.next()) {
        val id = resultSetJavi.getLong("id")
        val nombre = resultSetJavi.getString("nombre")
        val calificacion = resultSetJavi.getDouble("calificacion")
        val aprobado = resultSetJavi.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetJavi.getString("fecha_nacimiento"))
        alumnosJavi.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    alumnosJavi.forEach { println(it) }

    // Alumnos con id = 6
    val selectIdSQL = "SELECT * FROM alumnado WHERE id = 6"
    val resultSetId = statement.executeQuery(selectIdSQL)
    val alumnosId = mutableListOf<Alumno>()
    while (resultSetId.next()) {
        val id = resultSetId.getLong("id")
        val nombre = resultSetId.getString("nombre")
        val calificacion = resultSetId.getDouble("calificacion")
        val aprobado = resultSetId.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetId.getString("fecha_nacimiento"))
        alumnosId.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    val myAlumno = alumnosId.first()
    println("El alumno con id 6 es: $myAlumno")

    // Vamos a actualizar un alumno
    val alumnoUpdate = myAlumno.copy(calificacion = 5.0, aprobado = true)
    val updateAlumnoSQL = """
        UPDATE alumnado
        SET calificacion = ${alumnoUpdate.calificacion}, aprobado = ${alumnoUpdate.aprobado}
        WHERE id = ${alumnoUpdate.id};
    """.trimIndent()
    val resUpdate = statement.executeUpdate(updateAlumnoSQL)
    println("Filas afectadas: $resUpdate")

    // Todos los alumnos
    val selectAllSQL = "SELECT * FROM alumnado"
    val resultSetAll = statement.executeQuery(selectAllSQL)
    val alumnosAll = mutableListOf<Alumno>()
    while (resultSetAll.next()) {
        val id = resultSetAll.getLong("id")
        val nombre = resultSetAll.getString("nombre")
        val calificacion = resultSetAll.getDouble("calificacion")
        val aprobado = resultSetAll.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetAll.getString("fecha_nacimiento"))
        alumnosAll.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    alumnosAll.forEach { println(it) }

    // Borrar un alumno
    val deleteAlumnoSQL = "DELETE FROM alumnado WHERE id = 6"
    val resDelete = statement.executeUpdate(deleteAlumnoSQL)
    println("Filas afectadas: $resDelete")

    // Todos los alumnos
    val resultSetAll2 = statement.executeQuery(selectAllSQL)
    val alumnosAll2 = mutableListOf<Alumno>()
    while (resultSetAll2.next()) {
        val id = resultSetAll2.getLong("id")
        val nombre = resultSetAll2.getString("nombre")
        val calificacion = resultSetAll2.getDouble("calificacion")
        val aprobado = resultSetAll2.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetAll2.getString("fecha_nacimiento"))
        alumnosAll2.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    alumnosAll2.forEach { println(it) }

    // Saber cuantos alumnos hay
    val countAlumnosSQL = "SELECT COUNT(*) FROM alumnado"
    val resultSetCount = statement.executeQuery(countAlumnosSQL)
    val count = resultSetCount.getInt(1)
    println("Hay $count alumnos")

    // Cuantos alumnos aprobados hay
    val countAprobadosSQL = "SELECT COUNT(*) FROM alumnado WHERE aprobado = 1"
    val resultSetAprobados = statement.executeQuery(countAprobadosSQL)
    val countAprobados = resultSetAprobados.getInt(1)
    println("Hay $countAprobados alumnos aprobados")

    // Media de calificaciones
    val avgCalificacionesSQL = "SELECT AVG(calificacion) FROM alumnado"
    val resultSetAvg = statement.executeQuery(avgCalificacionesSQL)
    val avg = resultSetAvg.getDouble(1)
    println("La media de calificaciones es: $avg")

    // Calificación máxima
    val maxCalificacionesSQL = "SELECT MAX(calificacion) FROM alumnado"
    val resultSetMax = statement.executeQuery(maxCalificacionesSQL)
    val max = resultSetMax.getDouble(1)
    println("La máxima calificación es: $max")


    // Cerrar la conexión
    connection.close()


}