import { keycloak } from '@/plugins/authentication';
import router from '@/router';

export default async function (): Promise<string> {
    try {
        await keycloak.updateToken(70);
        //console.log("middleware keycloak: ", keycloak);
        return keycloak.token as string;
    } catch (err) {
        console.error(err);
        if (router.currentRoute.value.name !== 'unauthorized') {
            router.replace({ name: 'unauthorized' });
        }
        throw err;
    }
}