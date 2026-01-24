import { keycloak } from '@/plugins/authentication';

export default async function (): Promise<string> {
    await keycloak.updateToken(70);
    //console.log("middleware keycloak: ", keycloak);
    return keycloak.token as string;
}