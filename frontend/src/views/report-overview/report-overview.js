import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-select/src/vaadin-select.js';
import '@vaadin/vaadin-split-layout/src/vaadin-split-layout.js';
import '@vaadin/vaadin-grid/src/vaadin-grid.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';

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
   <div style="margin-left:16px; font-size: 16px;
    font-weight: 500; margin-right: 16px;">
     Report a bug 
   </div>
   <div style="margin-left:16px; font-size: 16px;
    font-weight: 500; margin-right: 16px;">
     Request a features 
   </div>
   <div style="margin-left:16px; font-size: 16px;
    font-weight: 500; margin-right: 16px;">
     Manage project 
   </div>
  </vaadin-horizontal-layout>
  <vaadin-horizontal-layout theme="margin" style="width: 50%; display: flex; align-items:center; justify-content: flex-end">
   <vaadin-text-field placeholder="Search report..." style="width : 80%" id="search">
    <iron-icon icon="lumo:search" slot="prefix"></iron-icon>
   </vaadin-text-field>
  </vaadin-horizontal-layout>
 </vaadin-horizontal-layout>
 <div style="width: 100%; height: 1px; background-color: black"></div>
 <vaadin-horizontal-layout theme="margin" style="width: 100%; display: flex; flex-wrap: wrap">
  <vaadin-horizontal-layout theme="margin" style="width: 100%; display:flex">
   <vaadin-horizontal-layout theme="margin" style="width: 20%; align-items: center;justify-content: space-between">
    <div style="margin-right: 16px">
      Report for 
    </div>
    <vaadin-select style="width:70%;" id="select-project"></vaadin-select>
   </vaadin-horizontal-layout>
   <vaadin-horizontal-layout theme="margin" style="width:80%; display:flex; align-items: center; justify-content: flex-end;  border-radius: 10px;">
    <vaadin-progress-bar style="width: 100%; height: 24px; color: grey" id="overview-project"></vaadin-progress-bar>
   </vaadin-horizontal-layout>
  </vaadin-horizontal-layout>
  <vaadin-horizontal-layout theme="margin" style="width: 100%; padding: 8px">
   <div>
     Assignees 
   </div>
   <vaadin-button theme="" id="btn-onlyme" style="margin-left: 16px ;margin-right: -4px;border-radius: 6px; width: 32px; height: 16px; font-size: 10px;">
     Only me 
   </vaadin-button>
   <vaadin-button theme="" style="margin-left: -4px;border-radius: 6px; width: 32px; height: 16px; font-size: 10px;" id="btn-everyone">
     Everyone 
   </vaadin-button>
   <div style="padding-left: 40px; padding-right: 12px">
     Status 
   </div>
   <vaadin-button style="margin-left: 16px ;margin-right: -20px;border-radius: 6px; width: 32px; height: 16px; font-size: 10px; " id="btn-open">
     Open 
   </vaadin-button>
   <vaadin-button style="margin-left: 16px ;margin-right: -20px;border-radius: 6px; width: 32px; height: 16px; font-size: 10px; " id="btn-allkinds">
     All kinds 
   </vaadin-button>
   <vaadin-button style="margin-left: 16px ;margin-right: -4px;border-radius: 6px; width: 32px; height: 16px; font-size: 10px; " id="btn-custom">
     Customs 
   </vaadin-button>
  </vaadin-horizontal-layout>
  <vaadin-split-layout orientation="vertical" style="width: 100%">
   <div id="wrapper-table">
    <vaadin-grid id="table" height-by-rows column-reordering-allowed></vaadin-grid>
   </div>
   <div id="wrapper-overview" style="width: 80%;">
    <vaadin-horizontal-layout theme="spacing" id="wrapper-info">
     <!--     <div id="infos-report" style="width: 80%;">-->
     <!--        Detail id: [[reportDetail.id]]<br/>-->
     <!--        Report id: [[reportDetail.reportId]]<br/>-->
     <!--        [[reportDetail.detail]] -->
     <!--    </div>-->
     <!--    <div id="infos-report2" style="width: 80%;"></div>-->
    </vaadin-horizontal-layout>
   </div>
  </vaadin-split-layout>
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
            persons: {
                observer: "_activeItemChanged",
            }
        };
    }

    _activeItemChanged(items) {
        //this.$.list.selectedItems = item ? [item] : [];
        // console.log(this.$)
        console.log(items);
//         if (items) {
//             this.$['infos-report2'].innerHTML = '';
//             items.map(i => {
//                 this.$['infos-report2'].innerHTML += `<div style="display:flex; justify-content:space-between; padding-bottom: 16px; padding-top: 16pxd ">
//     <div>  ${i.priority}</div>
//     <div>  ${i.assign}</div>
//     <div>  ${i.lastModified}</div>
//     <div>  ${i.summary}</div>
//     <div>  ${i.time}</div>
//     <div>  ${i.type}</div>
// </div>`
//
//             }
//         )
//         }


    }


}


customElements.define(ReportOverview.is, ReportOverview);
