/*
 * Copyright 2018 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.web.mock.graphql.web.graphql.controller;

import com.castlemock.core.mock.graphql.model.project.domain.GraphQLAttributeType;
import com.castlemock.core.mock.graphql.model.project.dto.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;


public class GraphQLResponseGenerator {

    private Random random = new Random();

    public String getResponse(final GraphQLProjectDto project,
                              final List<GraphQLRequestQueryDto> queries){
        OutputStream output = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) {
                this.string.append((char) b );
            }

            public String toString(){
                return this.string.toString();
            }
        };

        try {
            JsonFactory factory = new JsonFactory();
            JsonGenerator generator = factory.createGenerator(output);
            generator.setPrettyPrinter(new DefaultPrettyPrinter());

            generator.writeStartObject();
            generator.writeObjectFieldStart("data");

            for(GraphQLRequestQueryDto query : queries){
                printQuery(query, project, generator);
            }

            generator.writeEndObject();
            generator.writeEndObject();

            generator.flush();
            generator.close();
            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }




        return null;
    }

    private void printQuery(final GraphQLRequestQueryDto query,
                            final GraphQLProjectDto project,
                            final JsonGenerator generator) throws IOException {

        GraphQLQueryDto operation = getQuery(query.getOperationName(), project);
        GraphQLResultDto result = operation.getResult();
        GraphQLTypeDto type = getObject(result.getTypeName(), project);


        generator.writeFieldName(query.getOperationName());
        if(result.getListable()){
            generator.writeStartArray();
        }

        generator.writeStartObject();
        printType(type, query.getFields(), project, generator);
        generator.writeEndObject();

        if(result.getListable()){
            generator.writeEndArray();
        }


    }

    private void printType(final GraphQLTypeDto type,
                           final List<GraphQLRequestFieldDto> fields,
                           final GraphQLProjectDto project,
                           final JsonGenerator generator) throws IOException {

        if(type instanceof GraphQLObjectTypeDto){
            GraphQLObjectTypeDto objectType = (GraphQLObjectTypeDto) type;
            printObjectType(objectType, fields, project, generator);
        } else if(type instanceof GraphQLEnumTypeDto){
            GraphQLEnumTypeDto enumTypeDto = (GraphQLEnumTypeDto) type;
            printEnumType(enumTypeDto, generator);
        }
    }

    private void printObjectType(final GraphQLObjectTypeDto type,
                                 final List<GraphQLRequestFieldDto> fields,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException{
        if(fields != null && !fields.isEmpty()){
            for(GraphQLRequestFieldDto field : fields){
                GraphQLAttributeDto attribute = getAttribute(field.getName(), type);
                printAttribute(attribute, field.getFields(), project, generator);
            }
        } else {
            for(GraphQLAttributeDto attribute : type.getAttributes()){
                printAttribute(attribute, null, project, generator);
            }
        }


    }

    private void printEnumType(final GraphQLEnumTypeDto type,
                                 final JsonGenerator generator) throws IOException{
        final int enumSize = type.getDefinitions().size();
        final int enumIndex = random.nextInt(enumSize);
        final GraphQLEnumValueDefinitionDto definition = type.getDefinitions().get(enumIndex);

        generator.writeObject(definition.getName());
    }


    private void printAttribute(final GraphQLAttributeDto attribute,
                               final List<GraphQLRequestFieldDto> fields,
                            final GraphQLProjectDto project,
                            final JsonGenerator generator) throws IOException {
        GraphQLAttributeType type = attribute.getAttributeType();

        switch (type){
            case OBJECT_TYPE:
                printObjectType(attribute, fields, project, generator);
                break;
            case STRING:
                printString(attribute, project, generator);
                break;
            case ID:
                printId(attribute, project, generator);
                break;
            case INT:
                printInt(attribute, project, generator);
                break;
            case FLOAT:
                printFloat(attribute, project, generator);
                break;
            case BOOLEAN:
                printBoolean(attribute, project, generator);
                break;
            case ENUM:
                printEnum(attribute, project, generator);
                break;
        }
    }

    private void printStartType(final GraphQLAttributeDto attribute,
                                 final JsonGenerator generator) throws IOException {
        generator.writeFieldName(attribute.getName());

        if(attribute.getListable()){
            generator.writeStartArray();
        }
    }

    private void printEndType(final GraphQLAttributeDto attribute,
                                final JsonGenerator generator) throws IOException {
        if(attribute.getListable()){
            generator.writeEndArray();
        }
    }

    private void printObjectType(final GraphQLAttributeDto attribute,
                                 final List<GraphQLRequestFieldDto> fields,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException {
        GraphQLTypeDto type = getObject(attribute.getTypeName(),project);

        if(!(type instanceof GraphQLObjectTypeDto)){
            throw new IllegalArgumentException("Invalid type");
        }

        GraphQLObjectTypeDto objectType = (GraphQLObjectTypeDto) type;

        printStartType(attribute, generator);
        generator.writeStartObject();
        printObjectType(objectType, fields, project, generator);
        generator.writeEndObject();
        printEndType(attribute, generator);
    }

    private void printString(final GraphQLAttributeDto attribute,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException {
        printStartType(attribute, generator);
        generator.writeObject("String");
        printEndType(attribute, generator);
    }

    private void printId(final GraphQLAttributeDto attribute,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException {
        printStartType(attribute, generator);
        generator.writeObject("Id");
        printEndType(attribute, generator);
    }

    private void printInt(final GraphQLAttributeDto attribute,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException {
        printStartType(attribute, generator);
        generator.writeObject("Int");
        printEndType(attribute, generator);
    }

    private void printFloat(final GraphQLAttributeDto attribute,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException {
        printStartType(attribute, generator);
        generator.writeObject("Float");
        printEndType(attribute, generator);
    }

    private void printBoolean(final GraphQLAttributeDto attribute,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException {
        printStartType(attribute, generator);
        generator.writeObject("Boolean");
        printEndType(attribute, generator);
    }

    private void printEnum(final GraphQLAttributeDto attribute,
                                 final GraphQLProjectDto project,
                                 final JsonGenerator generator) throws IOException {
        GraphQLEnumTypeDto type = getEnum(attribute.getTypeName(),project);
        printStartType(attribute, generator);
        printEnumType(type, generator);
        printEndType(attribute, generator);
    }


    private GraphQLQueryDto getQuery(final String queryName,
                                           final GraphQLProjectDto project){
        for(GraphQLQueryDto query : project.getQueries()){
            if(query.getName().equals(queryName)){
                return query;
            }
        }

        return null;
    }

    private GraphQLTypeDto getObject(final String typeName,
                                final GraphQLProjectDto project){
        for(GraphQLObjectTypeDto type : project.getObjects()){
            if(type.getName().equals(typeName)){
                return type;
            }
        }

        return null;
    }

    private GraphQLEnumTypeDto getEnum(final String typeName,
                                     final GraphQLProjectDto project){
        for(GraphQLEnumTypeDto type : project.getEnums()){
            if(type.getName().equals(typeName)){
                return type;
            }
        }

        return null;
    }

    private GraphQLAttributeDto getAttribute(final String attributeName,
                                     final GraphQLObjectTypeDto type){
        for(GraphQLAttributeDto attribute : type.getAttributes()){
            if(attribute.getName().equals(attributeName)){
                return attribute;
            }
        }

        return null;
    }

}
