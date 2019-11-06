import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';

class ReportOverview extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                    width: 100%;
                }
                /*.vaadin-text-field-container : {*/
                /*    background-color: white;*/
                /*}*/
            </style>
<vaadin-vertical-layout style="width: 100%;">
 <vaadin-horizontal-layout theme="spacing" style="width: 100%; background-color:grey;height: 24px"></vaadin-horizontal-layout>
 <vaadin-horizontal-layout theme="margin" style="width: 100%; display: flex">
  <vaadin-combo-box id="vaadinComboBox" style="width: 50%; height: 32px;"></vaadin-combo-box>
  <vaadin-horizontal-layout theme="spacing" style="display: flex; width: 50%; height: 32px;justify-content: flex-end; margin-left: 8px; margin-right: 24px">
   <iron-icon icon="lumo:search" slot="prefix"></iron-icon>
   <vaadin-button id="vaadinButtonAccount" style="margin-left: 8px; margin-right: 8px; padding: 0;
border: none;
background: none;">
     Harry Fank Jefferson 
   </vaadin-button>
   <vaadin-button id="vaadinButtonLogout" style="margin-left: 8px; margin-right: 8px; padding: 0;
border: none;
background: none;">
    <iron-icon icon="lumo:search" slot="prefix"></iron-icon>
   </vaadin-button>
  </vaadin-horizontal-layout>
 </vaadin-horizontal-layout>
 <vaadin-horizontal-layout theme="margin" style="width: 100%; display: flex; align-items:">
  <vaadin-horizontal-layout theme="margin" style="width: 50%; display: flex; align-items:">
   <div style="margin-left:12px; font-size: 16px;
    font-weight: 500; margin-right: 12px;">
     Report a bug 
   </div>
   <div style="margin-left:12px; font-size: 16px;
    font-weight: 500; margin-right: 12px;">
     Request a features 
   </div>
   <div style="margin-left:12px; font-size: 16px;
    font-weight: 500; margin-right: 12px;">
     Manage project 
   </div>
  </vaadin-horizontal-layout>
  <vaadin-horizontal-layout theme="margin" style="width: 50%; display: flex; align-items:center; justify-content: flex-end">
   <vaadin-text-field placeholder="Search report..." style="width : 80%">
    <iron-icon icon="lumo:search" slot="prefix"></iron-icon>
   </vaadin-text-field>
  </vaadin-horizontal-layout>
 </vaadin-horizontal-layout>
</vaadin-vertical-layout>
`;
    }

    static get is() {
        return 'report-overview';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(ReportOverview.is, ReportOverview);
