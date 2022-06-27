import Vue from 'vue';

export default async function (): Promise<string> {
    await Vue.$keycloak.updateToken(70);
    //console.log("middleware keycloak: ", Vue.$keycloak);
    return Vue.$keycloak.token as string;
}