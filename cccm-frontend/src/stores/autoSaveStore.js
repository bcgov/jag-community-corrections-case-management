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
                            this.keyValueMap[i].questionId = dataArray[j].questionId;
                            break;
                        }        
                    }
                    if (!found) {
                        let newItem = {};
                        newItem.key = dataArray[j].key;
                        newItem.value = dataArray[j].value;
                        newItem.questionId = dataArray[j].questionId;
                        this.keyValueMap.push(newItem);  
                    }
                }
            }
        },
        getQuestionId(key) {
            for(let i = 0; i< this.keyValueMap.length; i++){    
                if(this.keyValueMap[i].key === key){
                    return this.keyValueMap[i].questionId;
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