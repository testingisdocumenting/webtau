package org.testingisdocumenting.webtau.graphql;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

import org.testingisdocumenting.webtau.graphql.config.GraphQLHttpConfiguration;
import org.testingisdocumenting.webtau.graphql.model.GraphQLRequest;

public class ConfigBasedGraphQLHttpConfiguration implements GraphQLHttpConfiguration {
    @Override
    public String requestUrl(String url, final GraphQLRequest graphQLRequest) {
        String endpoint = getCfg().get(GraphQLConfig.graphqlEndpoint.getKey());
        return endpoint + buildOperationQuery(graphQLRequest);
    }

    private String buildOperationQuery(final GraphQLRequest graphQLRequest) {
        if (getCfg().<Boolean>get(GraphQLConfig.showGraphqlOperationAsQueryParam.getKey())
            && null != graphQLRequest
            && null != graphQLRequest.getOperationName()
            && !graphQLRequest.getOperationName().isEmpty()) {
            return "?operation=" + graphQLRequest.getOperationName();
        } else {
            return "";
        }
    }
}
