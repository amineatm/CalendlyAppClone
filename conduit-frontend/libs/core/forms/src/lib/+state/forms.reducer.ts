import { Errors, Field } from './forms.interfaces';
import { formsActions } from './forms.actions';
import { createFeature, createReducer, on } from '@ngrx/store';

export interface NgrxFormsState {
  data: any;
  structure: Field[];
  valid: boolean;
  errors: Errors;
  touched: boolean;
}

export const ngrxFormsInitialState: NgrxFormsState = {
  data: {},
  structure: [],
  valid: true,
  errors: {},
  touched: false,
};

export const ngrxFormsFeature = createFeature({
  name: 'ngrxForms',
  reducer: createReducer(
    ngrxFormsInitialState,
    on(formsActions.setData, (state, action) => ({ ...state, data: action.data })),
    on(formsActions.updateData, (state, action) => {
      // const tagList =  typeof action.data.tagList == 'string' && action.data.tagList?.split(",").map((tag:any) => tag.trim())
      // const collaboratorList =  typeof action.data.collaboratorList == 'string' && action.data.collaboratorList?.split(",").map((collaborator:any) => collaborator.trim())
      // ,  tagList: tagList || action.data.tagList
      const data = { ...state.data, ...action.data };
      console.log(data);
      return { ...state, data, touched: true };
    }),
    on(formsActions.setStructure, (state, action) => ({ ...state, structure: action.structure.slice(0) })),
    on(formsActions.setErrors, (state, action) => ({ ...state, errors: action.errors })),
    on(formsActions.initializeErrors, (state) => ({ ...state, errors: {} })),
    on(formsActions.initializeForm, () => ngrxFormsInitialState),
    on(formsActions.resetForm, (state) => ({ ...state, touched: false })),
  ),
});
