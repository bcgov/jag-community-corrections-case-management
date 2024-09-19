import * as DateFns from 'date-fns';
export const DF_YYYY_MM_DD = 'yyyy-MM-dd';

export const dateToYYYY_MM_DD = (dateStr: string | null) => {
    return dateFormat(dateStr, DF_YYYY_MM_DD);
  };

const dateFormat = (dateStr: string | null, newDateFormat: string) => {
    //console.log("dateFormat: ", dateStr, newDateFormat);
    if (dateStr) {
      if (DateFns.isValid(new Date(dateStr)) === false) {
        return '';
      }
      let dateObject: Date = DateFns.parseISO(new Date(dateStr).toISOString());
      if (DateFns.isValid(dateObject) === false) {
        return '';
      }
      return DateFns.format(dateObject, newDateFormat);
    }
    return '';
  };