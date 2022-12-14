import {defineStore} from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'

export const useStore = defineStore('autosave', { 
    // state
    state: () =>({
        keyValueMap: []
    }),
    actions: {
        addArray(dataArray) {
            if (dataArray != null) {
                for(let j = 0; j < dataArray.length; j++){   
                    let found = false; 
                    for(let i = 0; i< this.keyValueMap.length; i++){    
                        if(this.keyValueMap[i].key === dataArray[j].key){
                            found = true;
                            this.keyValueMap[i].value = dataArray[j].value;
                            this.keyValueMap[i].dbID = dataArray[j].dbID;
                            break;
                        }        
                    }
                    if (!found) {
                        let newItem = {};
                        newItem.key = dataArray[j].key;
                        newItem.value = dataArray[j].value;
                        newItem.dbID = dataArray[j].dbID;
                        this.keyValueMap.push(newItem);  
                    }
                }
            }
        },
        getDBId(key) {
            for(let i = 0; i< this.keyValueMap.length; i++){    
                if(this.keyValueMap[i].key === key){
                    return this.keyValueMap[i].dbID;
                }        
            }
        },
        getValue(key) {
            for(let i = 0; i< this.keyValueMap.length; i++){    
                if(this.keyValueMap[i].key === key){
                    return this.keyValueMap[i].value;
                }        
            }
        }
    }
})