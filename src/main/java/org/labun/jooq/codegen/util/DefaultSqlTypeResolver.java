package org.labun.jooq.codegen.util;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;
import org.jooq.util.*;
import org.jooq.util.h2.H2DataType;
import org.labun.jooq.codegen.task.TaskContext;
import org.labun.jooq.codegen.JavaGenerator;

import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.IdentityHashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.name;

/**
 * Most of code in this class was originally taken from org.jooq.codegen project
 * just as is and need to be cleaned and rethinked.
 *
 * @author Konstantin Labun
 */
public class DefaultSqlTypeResolver implements TypeResolver {
    private static final JooqLogger LOG = JooqLogger.getLogger(JavaGenerator.class);

    /**
     * [#4429] A map providing access to SQLDataType member literals
     */
    private static final Map<DataType<?>, String> SQLDATATYPE_LITERAL_LOOKUP;


    static {
        SQLDATATYPE_LITERAL_LOOKUP = new IdentityHashMap<DataType<?>, String>();

        try {
            for (java.lang.reflect.Field f : SQLDataType.class.getFields()) {
                if (Modifier.isPublic(f.getModifiers()) &&
                        Modifier.isStatic(f.getModifiers()) &&
                        Modifier.isFinal(f.getModifiers()))
                    SQLDATATYPE_LITERAL_LOOKUP.put((DataType<?>) f.get(SQLDataType.class), f.getName());
            }
        } catch (Exception e) {
            LOG.warn(e);
        }
    }

    @Override
    public String resolve(TaskContext ctx, ColumnDefinition column) {
        return getJavaTypeReference(ctx, column.getDatabase(), column.getType());
    }

    protected String getTypeReference(TaskContext ctx, Database db, SchemaDefinition schema, String t, int p, int s, int l, boolean n, String d, Name u) {
        StringBuilder sb = new StringBuilder();
        if (db.getArray(schema, u) != null) {
            ArrayDefinition array = db.getArray(schema, u);

            sb.append(getJavaTypeReference(ctx, db, array.getElementType()));
            sb.append(".asArrayDataType(");
            sb.append(classOf(getStrategy().getFullJavaClassName(array, GeneratorStrategy.Mode.RECORD)));
            sb.append(")");
        } else if (db.getUDT(schema, u) != null) {
            sb.append(getStrategy().getFullJavaIdentifier(db.getUDT(schema, u)));
            sb.append(".getDataType()");
        }
        // [#3942] PostgreSQL treats UDTs and table types in similar ways
        // [#5334] In MySQL, the user type is (ab)used for synthetic enum types. This can lead to accidental matches here
        else if (db.getDialect().family() == POSTGRES && db.getTable(schema, u) != null) {
            sb.append(getStrategy().getFullJavaIdentifier(db.getTable(schema, u)));
            sb.append(".getDataType()");
        } else if (db.getEnum(schema, u) != null) {
            sb.append("org.jooq.util.");
            sb.append(db.getDialect().getName().toLowerCase());
            sb.append(".");
            sb.append(db.getDialect().getName());
            sb.append("DataType.");
            sb.append(DefaultDataType.normalise(DefaultDataType.getDataType(db.getDialect(), String.class).getTypeName()));
            sb.append(".asEnumDataType(");
            sb.append(classOf(getStrategy().getFullJavaClassName(db.getEnum(schema, u))));
            sb.append(")");
        } else {
            DataType<?> dataType = null;

            try {
                dataType = mapJavaTimeTypes(ctx, DefaultDataType.getDataType(db.getDialect(), t, p, s)).nullable(n);

                if (d != null)
                    dataType = dataType.defaultValue((Field) DSL.field(d, dataType));
            }

            // Mostly because of unsupported data types. Will be handled later.
            catch (SQLDialectNotSupportedException ignore) {
            }

            // If there is a standard SQLDataType available for the dialect-
            // specific DataType t, then reference that one.
            if (dataType != null && dataType.getSQLDataType() != null) {
                DataType<?> sqlDataType = dataType.getSQLDataType();
                String literal = SQLDATATYPE_LITERAL_LOOKUP.get(sqlDataType);
                String sqlDataTypeRef =
                        SQLDataType.class.getCanonicalName()
                                + '.'
                                + literal;

                sb.append(sqlDataTypeRef);

                if (dataType.hasPrecision() && p > 0) {
                    sb.append(".precision(").append(p);

                    if (dataType.hasScale() && s > 0)
                        sb.append(", ").append(s);

                    sb.append(")");
                }

                if (dataType.hasLength() && l > 0)
                    sb.append(".length(").append(l).append(")");

                if (!dataType.nullable())
                    sb.append(".nullable(false)");

                // [#5291] Some dialects report valid SQL expresions (e.g. PostgreSQL), others
                //         report actual values (e.g. MySQL).
                if (dataType.defaulted()) {
                    sb.append(".defaultValue(");

                    if (asList(MYSQL).contains(db.getDialect().family()))
                        sb.append("org.jooq.impl.DSL.inline(\"")
                                .append(escapeString(d))
                                .append("\"");
                    else
                        sb.append("org.jooq.impl.DSL.field(\"")
                                .append(escapeString(d))
                                .append("\"");

                    sb.append(", ")
                            .append(sqlDataTypeRef)
                            .append("))");
                }
            }

            // Otherwise, reference the dialect-specific DataType itself.
            else {
                try {

                    // [#4173] DEFAULT and SQL99 data types
                    String typeClass = (db.getDialect().getName() == null)
                            ? SQLDataType.class.getName()
                            : "org.jooq.util." +
                            db.getDialect().getName().toLowerCase() +
                            "." +
                            db.getDialect().getName() +
                            "DataType";

                    sb.append(typeClass);
                    sb.append(".");

                    String type1 = getType(ctx, db, schema, t, p, s, u, null, null);
                    String type2 = getType(ctx, db, schema, t, 0, 0, u, null, null);
                    String typeName = DefaultDataType.normalise(t);

                    // [#1298] Prevent compilation errors for missing types
                    Reflect.on(typeClass).field(typeName);

                    sb.append(typeName);
                    if (!type1.equals(type2)) {
                        Class<?> clazz = mapJavaTimeTypes(ctx, DefaultDataType.getDataType(db.getDialect(), t, p, s)).getType();

                        sb.append(".asNumberDataType(");
                        sb.append(classOf(clazz.getCanonicalName()));
                        sb.append(")");
                    }
                }

                // Mostly because of unsupported data types
                catch (SQLDialectNotSupportedException e) {
                    sb = new StringBuilder();

                    sb.append(DefaultDataType.class.getName());
                    sb.append(".getDefaultDataType(\"");
                    sb.append(t.replace("\"", "\\\""));
                    sb.append("\")");
                }

                // More unsupported data types
                catch (ReflectException e) {
                    sb = new StringBuilder();

                    sb.append(DefaultDataType.class.getName());
                    sb.append(".getDefaultDataType(\"");
                    sb.append(t.replace("\"", "\\\""));
                    sb.append("\")");
                }
            }
        }

        return sb.toString();
    }

    private DataType<?> mapJavaTimeTypes(TaskContext ctx, DataType<?> dataType) {
        DataType<?> result = dataType;


        // [#4429] [#5713] This LOGic should be implemented in Configuration
        if (dataType.isDateTime() && ctx.config().javaTimeDates()) {
            if (dataType.getType() == Date.class)
                result = SQLDataType.LOCALDATE;
            else if (dataType.getType() == Time.class)
                result = SQLDataType.LOCALTIME;
            else if (dataType.getType() == Timestamp.class)
                result = SQLDataType.LOCALDATETIME;
        }


        return result;
    }

    private String classOf(String string) {
        return string + ".class";
    }

    private String escapeString(String comment) {

        if (comment == null)
            return null;

        // [#3450] Escape also the escape sequence, among other things that break Java strings.
        return comment.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    protected String getJavaTypeReference(TaskContext ctx, Database db, DataTypeDefinition type) {

        // [#4388] TODO: Improve array handling
        if (db.isArrayType(type.getType())) {
            Name baseType = getArrayBaseType(db.getDialect(), type.getType(), type.getQualifiedUserType());
            return getTypeReference(ctx,
                    db,
                    type.getSchema(),
                    baseType.last(),
                    0,
                    0,
                    0,
                    true,
                    null,
                    baseType
            ) + ".getArrayDataType()";
        } else {
            return getTypeReference(ctx,
                    db,
                    type.getSchema(),
                    type.getType(),
                    type.getPrecision(),
                    type.getScale(),
                    type.getLength(),
                    type.isNullable(),
                    type.getDefaultValue(),
                    type.getQualifiedUserType()
            );
        }
    }

    @Deprecated
    protected String getType(TaskContext ctx, Database db, SchemaDefinition schema, String t, int p, int s, String u, String javaType, String defaultType) {
        return getType(ctx, db, schema, t, p, s, name(u), javaType, defaultType);
    }

    protected String getType(TaskContext ctx, Database db, SchemaDefinition schema, String t, int p, int s, Name u, String javaType, String defaultType) {
        return getType(ctx, db, schema, t, p, s, u, javaType, defaultType, GeneratorStrategy.Mode.RECORD);
    }

    @Deprecated
    protected String getType(TaskContext ctx, Database db, SchemaDefinition schema, String t, int p, int s, String u, String javaType, String defaultType, GeneratorStrategy.Mode udtMode) {
        return getType(ctx, db, schema, t, p, s, name(u), javaType, defaultType, udtMode);
    }

    protected String getType(TaskContext ctx, Database db, SchemaDefinition schema, String t, int p, int s, Name u, String javaType, String defaultType, GeneratorStrategy.Mode udtMode) {
        String type = defaultType;

        // Custom types
        if (javaType != null) {
            type = javaType;
        }

        // Array types
        else if (db.isArrayType(t)) {

            // [#4388] TODO: Improve array handling
            Name baseType = getArrayBaseType(db.getDialect(), t, u);

            type = getType(ctx, db, schema, baseType.last(), p, s, baseType, javaType, defaultType, udtMode) + "[]";
        }

        // Check for Oracle-style VARRAY types
        else if (db.getArray(schema, u) != null) {
            boolean udtArray = db.getArray(schema, u).getElementType().isUDT();

            if (udtMode == GeneratorStrategy.Mode.POJO || (udtMode == GeneratorStrategy.Mode.INTERFACE && !udtArray)) {
                type = "java.util.List<" + getJavaType(ctx, db.getArray(schema, u).getElementType(), udtMode) + ">";
            } else if (udtMode == GeneratorStrategy.Mode.INTERFACE) {
                type = "java.util.List<? extends " + getJavaType(ctx, db.getArray(schema, u).getElementType(), udtMode) + ">";
            } else {
                type = getStrategy().getFullJavaClassName(db.getArray(schema, u), GeneratorStrategy.Mode.RECORD);
            }
        }

        // Check for ENUM types
        else if (db.getEnum(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getEnum(schema, u));
        }

        // Check for UDTs
        else if (db.getUDT(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getUDT(schema, u), udtMode);
        }

        // [#3942] PostgreSQL treats UDTs and table types in similar ways
        // [#5334] In MySQL, the user type is (ab)used for synthetic enum types. This can lead to accidental matches here
        else if (db.getDialect().family() == POSTGRES && db.getTable(schema, u) != null) {
            type = getStrategy().getFullJavaClassName(db.getTable(schema, u), udtMode);
        }

        // Check for custom types
        else if (u != null && db.getConfiguredCustomType(u.last()) != null) {
            type = u.last();
        }

        // Try finding a basic standard SQL type according to the current dialect
        else {
            try {
                Class<?> clazz = mapJavaTimeTypes(ctx, DefaultDataType.getDataType(db.getDialect(), t, p, s)).getType();
                type = clazz.getCanonicalName();

                if (clazz.getTypeParameters().length > 0) {
                    type += "<";

                    String separator = "";
                    for (TypeVariable<?> var : clazz.getTypeParameters()) {
                        type += separator;
                        type += ((Class<?>) var.getBounds()[0]).getCanonicalName();

                        separator = ", ";
                    }

                    type += ">";
                }
            } catch (SQLDialectNotSupportedException e) {
                if (defaultType == null) {
                    throw e;
                }
            }
        }

        return type;
    }

    public GeneratorStrategy getStrategy() {
        return new DefaultGeneratorStrategy();
    }

    /**
     * Gets the base type for an array type, depending on the RDBMS dialect
     */
    static Name getArrayBaseType(SQLDialect dialect, String t, Name u) {

        // [#4388] TODO: Improve array handling
        switch (dialect.family()) {


            case POSTGRES: {

                // The convention is to prepend a "_" to a type to get an array type
                if (u != null && u.last().startsWith("_")) {
                    String[] name = u.getName();
                    name[name.length - 1] = name[name.length - 1].substring(1);
                    return name(name);
                }

                // But there are also arrays with a "vector" suffix
                else {
                    return u;
                }
            }

            case H2: {
                return name(H2DataType.OTHER.getTypeName());
            }


            case HSQLDB: {

                // In HSQLDB 2.2.5, there has been an incompatible INFORMATION_SCHEMA change around the
                // ELEMENT_TYPES view. Arrays are now described much more explicitly
                if ("ARRAY".equalsIgnoreCase(t)) {
                    return name("OTHER");
                }

                // This is for backwards compatibility
                else {
                    return name(t.replace(" ARRAY", ""));
                }
            }
        }

        throw new SQLDialectNotSupportedException("getArrayBaseType() is not supported for dialect " + dialect);
    }

    protected String getJavaType(TaskContext ctx, DataTypeDefinition type) {
        return getJavaType(ctx, type, GeneratorStrategy.Mode.RECORD);
    }

    protected String getJavaType(TaskContext ctx, DataTypeDefinition type, GeneratorStrategy.Mode udtMode) {
        return getType(ctx,
                type.getDatabase(),
                type.getSchema(),
                type.getType(),
                type.getPrecision(),
                type.getScale(),
                type.getQualifiedUserType(),
                type.getJavaType(),
                Object.class.getName(),
                udtMode);
    }
}
