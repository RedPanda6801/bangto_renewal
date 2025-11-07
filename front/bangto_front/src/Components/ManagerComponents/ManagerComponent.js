import React, { useState } from 'react';
import '../ManagerComponent.css';
import ManagerApplyComponent from "./ManagerApplyComponent";
import ManagerUserComponent from './ManagerUserComponent';
import ManagerGroupComponent from './ManagerGroupComponent';
import ManagerStoreComponent from './ManagerStoreComponent';

const ManagerComponent = () => 
{
    const [selectedMenu, setSelectedMenu] = useState("고객 관리");


    const renderContent = (menu) => {
        if("고객 관리" == menu) {
            return <ManagerUserComponent></ManagerUserComponent>
            
        } else if("판매자 / 매장 관리" == menu) {

            return <ManagerStoreComponent></ManagerStoreComponent>
        }
        else if("판매자 인증 신청 관리" == menu) {
            return <ManagerApplyComponent></ManagerApplyComponent>
        }
        else if("공동 구매 관리" == menu) {
        //{searchGroupDate}
            return <ManagerGroupComponent></ManagerGroupComponent>
        } else {
            return <>선택된 메뉴 없음</>;
        }
      };

    return (        
        <>
        <div className="layout_Manager">
            <div className="box_Manager_Menu">
                <button
                    className="btn_ManagerMenu"
                    onClick={() => {setSelectedMenu("고객 관리");}}>
                    고객 관리
                </button>
                <button
                    className="btn_ManagerMenu"
                    onClick={() => {setSelectedMenu("판매자 / 매장 관리");}}>
                    판매자 / 매장 관리
                </button>
                <button
                    className="btn_ManagerMenu"
                    onClick={() => setSelectedMenu("판매자 인증 신청 관리")}>
                    판매자 인증 신청 관리
                </button>
                <button
                    className="btn_ManagerMenu"
                    onClick={() => setSelectedMenu("공동 구매 관리")}>
                    공동 구매 관리
                </button>
            </div>
            <div className="layout_Manager_Contents">
                {renderContent(selectedMenu)}
            </div>
        </div>
    </>
    );
}

export default ManagerComponent;