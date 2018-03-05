package com.castlemock.web.mock.graphql.converter.query;

import com.castlemock.core.mock.graphql.model.project.dto.GraphQLRequestArgumentDto;
import com.castlemock.core.mock.graphql.model.project.dto.GraphQLRequestFieldDto;
import com.castlemock.core.mock.graphql.model.project.dto.GraphQLRequestQueryDto;
import graphql.language.*;
import graphql.parser.Parser;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ArrayList;
import java.util.List;

public class QueryGraphQLConverter {


    public List<GraphQLRequestQueryDto> parseQuery(final String body){
        Parser parser = new Parser();
        Document document;
        try {
            document = parser.parseDocument(body);
            document.getChildren();

            for(Definition definition : document.getDefinitions()){

                if(definition instanceof OperationDefinition){
                    OperationDefinition operationDefinition = (OperationDefinition) definition;
                    OperationDefinition.Operation operation = operationDefinition.getOperation();

                    if(operation == OperationDefinition.Operation.QUERY){
                        return getQuery(operationDefinition);
                    }

                }

            }

        } catch (ParseCancellationException e) {
            System.out.println(e);
        }
        return null;
    }


    private List<GraphQLRequestQueryDto> getQuery(final OperationDefinition operationDefinition){
        final List<GraphQLRequestQueryDto> queries = new ArrayList<>();
        for(Selection selection : operationDefinition.getSelectionSet().getSelections()){
            if(selection instanceof Field){
                Field field = (Field) selection;

                final GraphQLRequestQueryDto query = new GraphQLRequestQueryDto();
                final List<GraphQLRequestFieldDto> fields = new ArrayList<>();
                final List<GraphQLRequestArgumentDto> arguments = new ArrayList<>();
                query.setOperationName(field.getName());
                query.setFields(fields);
                query.setArguments(arguments);

                for(Selection subSelection : field.getSelectionSet().getSelections()){
                    if(subSelection instanceof Field){
                        Field subField = (Field) subSelection;
                        GraphQLRequestFieldDto subRequestField = getField(subField);
                        fields.add(subRequestField);
                    }
                }

                for(Argument argument : field.getArguments()){
                    GraphQLRequestArgumentDto requestArgument = getArgument(argument);
                    arguments.add(requestArgument);
                }

                queries.add(query);

            }
        }

        return queries;
    }

    private GraphQLRequestFieldDto getField(Field field){
        final GraphQLRequestFieldDto requestField = new GraphQLRequestFieldDto();
        final List<GraphQLRequestFieldDto> fields = new ArrayList<>();
        final List<GraphQLRequestArgumentDto> arguments = new ArrayList<>();
        requestField.setName(field.getName());
        requestField.setFields(fields);

        if(field.getSelectionSet() != null && field.getSelectionSet().getSelections() != null){
            for(Selection selection : field.getSelectionSet().getSelections()){
                if(selection instanceof Field){
                    Field subField = (Field) selection;
                    GraphQLRequestFieldDto subRequestField = getField(subField);
                    fields.add(subRequestField);
                }
            }
        }

        for(Argument argument : field.getArguments()){
            GraphQLRequestArgumentDto requestArgument = getArgument(argument);
            arguments.add(requestArgument);
        }


        return requestField;
    }

    private GraphQLRequestArgumentDto getArgument(final Argument argument){
        final GraphQLRequestArgumentDto requestArgument = new GraphQLRequestArgumentDto();
        requestArgument.setName(argument.getName());
        return requestArgument;

    }

}
