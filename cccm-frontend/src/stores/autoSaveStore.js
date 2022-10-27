import {defineStore} from 'pinia';
import { useLocalStorage, useSessionStorage } from '@vueuse/core'

export const useStore = defineStore('autosave', { 
    // state
    state: () =>({
        keyValueMap: [{key: '', value: ''}]
    }),
    actions: {
        addOne(key, value) {
            let found = false;
            for(i = 0; i< keyValueMap.length; i++){    
                if(keyValueMap[i].key === key){
                    found = true;
                    break;
                }        
            }
            if (!found) {
                let newItem = {};
                newItem.key = key;
                newItem.value = value;
                keyValueMap.push(newItem);  
            }
        },
        addArray(dataArray) {
            if (dataArray != null) {
                for(j = 0; j < dataArray.length; j++){   
                    let found = false; 
                    for(i = 0; i< keyValueMap.length; i++){    
                        if(keyValueMap[i].key === dataArray[j].key){
                            found = true;
                            break;
                        }        
                    }
                    if (!found) {
                        let newItem = {};
                        newItem.key = dataArray[j].key;
                        newItem.value = dataArray[j].value;
                        keyValueMap.push(newItem);  
                    }
                }
            }
        },
        getValue(key) {
            for(i = 0; i< keyValueMap.length; i++){    
                if(keyValueMap[i].key === key){
                    return keyValueMap[i].value;
                }        
            }
        }
    }
})