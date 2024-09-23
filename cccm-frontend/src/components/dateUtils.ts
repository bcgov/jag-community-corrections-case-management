import * as DateFns from 'date-fns';
export const DF_YYYY_MM_DD = 'yyyy-MM-dd';
export const DF_CCCM_STANDARD = 'yyyy.MM.dd';

export const dateToYYYY_MM_DD = (dateStr: string | null) => {
    return dateFormat(dateStr, DF_YYYY_MM_DD);
};

export const dateToCCCMDateformat = (dateStr: string | null) => {
    return dateFormat(dateStr, DF_CCCM_STANDARD);
};

const isDateContainTime = (dateStr : string) => {   
    //console.log("isDateContainTime dateStr: ", dateStr) 
    let dateSplit = dateStr.split(' ');
    // date contains time portion
    if (dateSplit && dateSplit.length > 1) {
        return true;
    } else {
        dateSplit = dateStr.split('T');
        if (dateSplit && dateSplit.length > 1) {
            return true;
        }
        return false;
    }
}

const dateFormat = (dateStr: string | null, newDateFormat: string) => {
    //console.log("dateFormat: ", dateStr, newDateFormat);
    if (dateStr) {
        if (!isDateContainTime(dateStr)) {
            // append time portion
            dateStr = dateStr + 'T00:00:00'
        } 

        var date = new Date(dateStr);
        if (DateFns.isValid(date) === false) {
            return '';
        }      
        let dateObject: Date = DateFns.parseISO(date.toISOString());
        if (DateFns.isValid(dateObject) === false) {
            return '';
        }
        return DateFns.format(dateObject, newDateFormat);
    }
    return '';
  };