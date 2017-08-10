///////////////////////////////////////////////////////////////////////////////
// FILE:          ThorlabsMFF.cpp
// PROJECT:       Micro-Manager
// SUBSYSTEM:     DeviceAdapters
//-----------------------------------------------------------------------------
// DESCRIPTION:   Control of Thorlabs stages using the APT library
//
// COPYRIGHT:     Emilio J. Gualda, 2012
//                Egor Zindy, University of Manchester, 2013
//
// LICENSE:       This file is distributed under the BSD license.
//
//                This file is distributed in the hope that it will be useful,
//                but WITHOUT ANY WARRANTY; without even the implied warranty
//                of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
//                IN NO EVENT SHALL THE COPYRIGHT OWNER OR
//                CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
//                INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
//
// AUTHOR:        Emilio J. Gualda, IGC, 2012
//                Egor Zindy (egor.zindy@manchester.ac.uk)
//                Contributions and testing (TDC001): Alfie O'Neill / Christopher Blount
//
// NOTES:         Needs / Tested with APT.dll v 1.0.0.3
//                The following APT controllers have been tested:
//
//                [ ] BSC001 - 1 Ch benchtop stepper driver
//                [ ] BSC101 - 1 Ch benchtop stepper driver
//                [ ] BSC002 - 2 Ch benchtop stepper driver
//                [ ] BDC101 - 1 Ch benchtop DC servo driver
//                [ ] SCC001 - 1 Ch stepper driver card (used within BSC102,103 units)
//                [ ] DCC001 - 1 Ch DC servo driver card (used within BDC102,103 units)
//                [ ] ODC001 - 1 Ch DC servo driver cube
//                [ ] OST001 - 1 Ch stepper driver cube
//                [ ] MST601 - 2 Ch modular stepper driver module
//                [ ] TST001 - 1 Ch Stepper driver T-Cube
//                [X] TDC001 - 1 Ch DC servo driver T-Cube
//

#ifdef WIN32
   #include <windows.h>
   #define snprintf _snprintf
#endif

#include "ThorlabsMFF.h"
#include "APTAPI.h"
#include <cstdio>
#include <string>
#include <math.h>
#include <sstream>
#include <string.h>

//Short descriptions taken from the APTAPI.h
//const char* g_ThorlabsDeviceNameBSC001 = "BSC001";
//const char* g_ThorlabsDeviceDescBSC001 = "1 Ch benchtop stepper driver";
//const char* g_ThorlabsDeviceNameBSC101 = "BSC101";
//const char* g_ThorlabsDeviceDescBSC101 = "1 Ch benchtop stepper driver";
//const char* g_ThorlabsDeviceNameBSC002 = "BSC002";
//const char* g_ThorlabsDeviceDescBSC002 = "2 Ch benchtop stepper driver";
//const char* g_ThorlabsDeviceNameBDC101 = "BDC101";
//const char* g_ThorlabsDeviceDescBDC101 = "1 Ch benchtop DC servo driver";
//const char* g_ThorlabsDeviceNameSCC001 = "SCC001";
//const char* g_ThorlabsDeviceDescSCC001 = "1 Ch stepper driver card (used within BSC102,103 units)";
//const char* g_ThorlabsDeviceNameDCC001 = "DCC001";
//const char* g_ThorlabsDeviceDescDCC001 = "1 Ch DC servo driver card (used within BDC102,103 units)";
//const char* g_ThorlabsDeviceNameODC001 = "ODC001";
//const char* g_ThorlabsDeviceDescODC001 = "1 Ch DC servo driver cube";
//const char* g_ThorlabsDeviceNameOST001 = "OST001";
//const char* g_ThorlabsDeviceDescOST001 = "1 Ch stepper driver cube";
//const char* g_ThorlabsDeviceNameMST601 = "MST601";
//const char* g_ThorlabsDeviceDescMST601 = "2 Ch modular stepper driver module";
//const char* g_ThorlabsDeviceNameTST001 = "TST001";
//const char* g_ThorlabsDeviceDescTST001 = "1 Ch Stepper driver T-Cube";
//const char* g_ThorlabsDeviceNameTDC001 = "TDC001";
//const char* g_ThorlabsDeviceDescTDC001 = "1 Ch DC servo driver T-Cube";
const char* g_ThorlabsDeviceNameMFF    = "MFF101/MFF102";
const char* g_ThoralbsDeviceDescMFF    = "Motorised Filter Flipper";

const char* g_PositionProp = "Position";
const char* g_Keyword_Position = "Set position (microns)";
const char* g_Keyword_Velocity = "Velocity (mm/s)";
const char* g_Keyword_Home="Go Home";

const char* g_NumberUnitsProp = "Number of Units";
const char* g_SerialNumberProp = "Serial Number";
const char* g_ChannelProp = "Channel";
const char* g_MaxVelProp = "Maximum Velocity";
const char* g_MaxAccnProp = "Maximum Acceleration";
const char* g_MinPosProp = "Position Lower Limit (um)";
const char* g_MaxPosProp = "Position Upper Limit (um)";
const char* g_StepSizeProp = "Step Size";

using namespace std;

///////////////////////////////////////////////////////////////////////////////
// Exported MMDevice API
///////////////////////////////////////////////////////////////////////////////

MODULE_API void InitializeModuleData()
{
    int hola;
    long plNumUnits;

    if (aptInitialized == false)
    {
        APTCleanUp();
        hola = APTInit();
        if (hola != 0) {
            APTCleanUp();
            return;
        }
    }

    aptInitialized = true;

 	//MFF101(/M),MFF102(M)
	hola = GetNumHWUnitsEx(48, &plNumUnits);
	if (plNumUnits >= 1) RegisterDevice(g_ThorlabsDeviceNameMFF, MM::StageDevice, g_ThoralbsDeviceDescMFF);

}

MODULE_API MM::Device* CreateDevice(const char* deviceName)
{
    int hwType;
    int chNumber = 1;

    if (deviceName == 0)
        return 0;

    else if (strcmp(deviceName, g_ThorlabsDeviceNameMFF) == 0)
    {
        hwType = 48;
    }
    else
        return 0;

    ThorlabsMFF* s = new ThorlabsMFF(hwType,deviceName,chNumber);
    return s;
}

MODULE_API void DeleteDevice(MM::Device* pDevice)
{
    delete pDevice;
}

///////////////////////////////////////////////////////////////////////////////
// ThorlabsMFF class
// FIXME? as default initialises to TDC001 / 1 channel
//
ThorlabsMFF::ThorlabsMFF() :
    hwType_(0),
    deviceName_("ThorlabsMFF"),
    chNumber_(1),
    serialNumber_(0),
    stepSizeUm_(0.1),
    initialized_(false),
    busy_(false),
    homed_(false),
    answerTimeoutMs_(1000),
    minTravelUm_(0.0),
    maxTravelUm_(500000.0),
    curPosUm_(0.0)

{
    //use the TDC001 default
    int hwType = 48;
	std::string deviceName = g_ThoralbsDeviceDescMFF

    init(hwType, deviceName, chNumber);
}

ThorlabsMFF::ThorlabsMFF(int hwType, std::string deviceName, long chNumber) :
    hwType_(0),
    deviceName_("ThorlabsMFF"),
    chNumber_(1),
    serialNumber_(0),
    stepSizeUm_(0.1),
    initialized_(false),
    busy_(false),
    homed_(false),
    answerTimeoutMs_(1000),
    minTravelUm_(0.0),
    maxTravelUm_(500000.0),
    curPosUm_(0.0)
{
    init(hwType, deviceName, chNumber);
}

ThorlabsMFF::~ThorlabsMFF()
{
    Shutdown();
}

void ThorlabsMFF::GetName(char* Name) const
{
    CDeviceUtils::CopyLimitedString(Name, deviceName_.c_str());
}

int ThorlabsMFF::Initialize()
{
    int ret(DEVICE_OK);
    int hola=0;

    //needed to get the hardware info
    char szModel[256], szSWVer[256], szHWNotes[256];

    //change the global flag
    if (aptInitialized == false)
    {
        //Initialize various data structures, initialise USB bus and other start functions
        APTCleanUp();
        hola=APTInit();
        if (hola != 0)
        {
            APTCleanUp();
            return hola;
        }
    }

    aptInitialized = true;
    LogInit();

    tmpMessage << "InitHWDevice()";
    LogIt();

    EnableEventDlg(TRUE);
    InitHWDevice((long)serialNumber_);
    MOT_SetChannel(serialNumber_, chNumber_-1);

    MOT_GetVelParamLimits(serialNumber_, &pfMaxAccn, &pfMaxVel);

    tmpMessage << "pfMaxAccn:" << pfMaxAccn << " pfMaxVel:" << pfMaxVel;
    LogIt();

    //GetLimits(minTravelUm,maxTravelUm);

    //Initialising pfMinPos and pfMaxPos
    MOT_GetStageAxisInfo (serialNumber_, &pfMinPos, &pfMaxPos, &plUnits, &pfPitch);
    tmpMessage << "pfMinPos:" << pfMinPos << " pfMaxPos:" << pfMaxPos << " plUnits:" << plUnits << " pfPitch:" << pfPitch;
    LogIt();

    // The device description is updated using the APT library
    GetHWInfo(serialNumber_, szModel, 256, szSWVer, 256, szHWNotes, 256);
    SetProperty(MM::g_Keyword_Description,szHWNotes);

    tmpMessage << "szModel:" << szModel << " szSWVer:" << szSWVer << " szHWNotes:" << szHWNotes;
    LogIt();

    // READ ONLY PROPERTIES
    CreateProperty(g_SerialNumberProp,CDeviceUtils::ConvertToString(serialNumber_),MM::String,true);
    CreateProperty(g_MaxVelProp,CDeviceUtils::ConvertToString(pfMaxVel),MM::String,true);
    CreateProperty(g_MaxAccnProp,CDeviceUtils::ConvertToString(pfMaxAccn),MM::String,true);

    //Action Properties
    CPropertyAction* pAct = new CPropertyAction (this, &ThorlabsMFF::OnPosition);
    CPropertyAction* pAct2 = new CPropertyAction (this, &ThorlabsMFF::OnVelocity);
    CPropertyAction* pAct3 = new CPropertyAction (this, &ThorlabsMFF::OnHome);
    CPropertyAction* pAct4 = new CPropertyAction (this, &ThorlabsMFF::OnMinPosUm);
    CPropertyAction* pAct5 = new CPropertyAction (this, &ThorlabsMFF::OnMaxPosUm);

    CreateProperty(g_Keyword_Position, CDeviceUtils::ConvertToString(minTravelUm_), MM::Float, false, pAct);
    SetPropertyLimits(g_Keyword_Position, minTravelUm_, maxTravelUm_);

    CreateProperty(g_Keyword_Velocity, CDeviceUtils::ConvertToString(pfMaxVel), MM::Float, false, pAct2);

    CreateProperty(g_Keyword_Home, "0", MM::Integer, false, pAct3);
    SetPropertyLimits(g_Keyword_Home, 0, 1);

    //By now, we now more about the hardware. Lets set proper hardware limits for g_MinPosProp / g_MaxPosProp
    //pfMinPos*1000 / pfMaxPos*1000 are the absolute limits.
    //FIXME may run into problems if not (plUnits == STAGE_UNITS_MM || plUnits == STAGE_UNITS_DEG)

    CreateProperty(g_MinPosProp, CDeviceUtils::ConvertToString(minTravelUm_), MM::Float, false, pAct4);
    SetPropertyLimits(g_MinPosProp, pfMinPos*1000, pfMaxPos*1000);

    CreateProperty(g_MaxPosProp, CDeviceUtils::ConvertToString(maxTravelUm_), MM::Float, false, pAct5);
    SetPropertyLimits(g_MaxPosProp, pfMinPos*1000, pfMaxPos*1000);

    ret = UpdateStatus();

    tmpMessage << "all done";
    LogIt();

    if (ret != DEVICE_OK)
        return ret;

    initialized_ = true;
    return DEVICE_OK;
}

int ThorlabsMFF::Shutdown()
{

    if (initialized_)
    {
        tmpMessage << "Shutdown()";
        LogIt();

        initialized_ = false;
        aptInitialized = false;
        APTCleanUp();
    }
    return DEVICE_OK;
}

bool ThorlabsMFF::Busy()
{
    // never busy because all commands block
    return false;
}

int ThorlabsMFF::GetPositionUm(double& posUm)
{

    MOT_SetChannel(serialNumber_, chNumber_-1);
    MOT_GetPosition(serialNumber_, &pfPosition);
    posUm=pfPosition*1000;
    curPosUm_=(float)posUm;

    tmpMessage << "GetPositionUm:" <<curPosUm_;
    LogIt();

    return DEVICE_OK;
}

int ThorlabsMFF::SetPositionUm(double posUm)
{
    return SetPositionUmFlag(posUm, 1);
}

int ThorlabsMFF::SetPositionUmContinuous(double posUm)
{
    return SetPositionUmFlag(posUm, 0);
}

int ThorlabsMFF::SetOrigin()
{
    return DEVICE_UNSUPPORTED_COMMAND;
}

int ThorlabsMFF::SetPositionSteps(long steps)
{
    steps=(long)0.1;
    return DEVICE_OK;
}

int ThorlabsMFF::GetPositionSteps(long& steps)
{
    stepSizeUm_=steps;
    return DEVICE_OK;
}

int ThorlabsMFF::GetLimits(double& min, double& max)
{
    /*
    MOT_SetChannel(serialNumber_, chNumber_-1);
    MOT_GetStageAxisInfo (serialNumber_, &pfMinPos, &pfMaxPos, &plUnits, &pfPitch);

    tmpMessage << "In GetLimits(). chNumber:" << chNumber << " pfMinPos:" << pfMinPos << " pfMaxPos:" << pfMaxPos << " plUnits:" << plUnits << " pfPitch:" << pfPitch;
    LogIt();
    */

    min = minTravelUm_;
    max = maxTravelUm_;

    return DEVICE_OK;
}

int ThorlabsMFF::SetLimits(double min, double max)
{
    minTravelUm_ = min;
    maxTravelUm_ = max;
    return DEVICE_OK;
}


///////////////////////////////////////////////////////////////////////////////
// private methods
///////////////////////////////////////////////////////////////////////////////

void ThorlabsMFF::init(int hwType, std::string deviceName, long chNumber)
{
    long plNumUnits, plSerialNum;
    int hola;

    hwType_ = hwType;
    deviceName_ = deviceName;
    chNumber_ = chNumber;

    InitializeDefaultErrorMessages();

    // set device specific error messages
    // ------------------------------------
    SetErrorText(ERR_UNRECOGNIZED_ANSWER, "Invalid response from the device.");
    SetErrorText(ERR_UNSPECIFIED_ERROR, "Unspecified error occured.");
    SetErrorText(ERR_RESPONSE_TIMEOUT, "Device timed-out: no response received withing expected time interval.");
    SetErrorText(ERR_BUSY, "Device busy.");
    SetErrorText(ERR_STAGE_NOT_ZEROED, "Zero sequence still in progress.\n"
                                       "Wait for few more seconds before trying again."
                                       "Zero sequence executes only once per power cycle.");

   // create pre-initialization properties
   // ------------------------------------

   // Name
   CreateProperty(MM::g_Keyword_Name, deviceName.c_str(), MM::String, true);

   // Description (will be properly updated later from MOT_GetStageAxisInfo)
   CreateProperty(MM::g_Keyword_Description, "Thorlabs Stage", MM::String, true);

   // Serial Number
   CPropertyAction* pAct1 = new CPropertyAction (this, &ThorlabsMFF::OnSerialNumber);
   CreateProperty(g_SerialNumberProp, "", MM::Integer, false, pAct1, true);

   if (aptInitialized == false)
   {
        //Initialize various data structures, initialise USB bus and other start functions
        APTCleanUp();
        hola=APTInit();
   }

    //Get the number of devices...
    hola = GetNumHWUnitsEx(hwType_, &plNumUnits);
    if (plNumUnits < 1)
    {
        APTCleanUp();
        return;
    }

    //change the global flag
    aptInitialized = true;

    //Populating the Serial Number drop-down menu (select the first one via setting serialNumber_)
    hola = 0;
    EnableEventDlg(TRUE);

    for (int i = 0; i < plNumUnits && i < 64; i++)
    {
        hola = GetHWSerialNumEx(hwType_, i, &plSerialNum);
        if (i == 0) serialNumber_ = plSerialNum;

        AddAllowedValue(g_SerialNumberProp, CDeviceUtils::ConvertToString(plSerialNum));
    }

    //Populating the Channel drop-down menu (last one selected by default?)
    CPropertyAction* pAct2 = new CPropertyAction (this, &ThorlabsMFF::OnChannelNumber);
    CreateProperty(g_ChannelProp, "1", MM::Integer, false, pAct2, true);

    for (int i = 0; i < chNumber_; i++)
    {
        AddAllowedValue(g_ChannelProp, CDeviceUtils::ConvertToString(i+1));
    }
    chNumber_ = 1;

    //Populating the min and max values for min travel

    //At this point in time, we do not know the real min and max travel.
    //These depend on the serial number and channel and type of stage attached.
    //Widely, 0-500000 um should cover the rotation stages as well (0-360*1000?)
    pfMinPos = (float)(minTravelUm_ / 1000);
    pfMaxPos = (float)(maxTravelUm_ / 1000);

    CPropertyAction* pAct3 = new CPropertyAction (this, &ThorlabsMFF::OnMinPosUm);
    CreateProperty(g_MinPosProp, CDeviceUtils::ConvertToString(minTravelUm_), MM::Float, false, pAct3, true);
    SetPropertyLimits(g_MinPosProp, minTravelUm_, maxTravelUm_);

    CPropertyAction* pAct4 = new CPropertyAction (this, &ThorlabsMFF::OnMaxPosUm);
    CreateProperty(g_MaxPosProp, CDeviceUtils::ConvertToString(maxTravelUm_), MM::Float, false, pAct4, true);
    SetPropertyLimits(g_MaxPosProp, minTravelUm_, maxTravelUm_);
}

void ThorlabsMFF::LogInit()
{
    tmpMessage = std::stringstream();
    tmpMessage << deviceName_ << "-" << serialNumber_ << " ";
}

void ThorlabsMFF::LogIt()
{
    LogMessage(tmpMessage.str().c_str());
    LogInit();
}

int ThorlabsMFF::SetPositionUmFlag(double posUm, int continuousFlag)
{
    if (posUm < minTravelUm_)
        posUm = minTravelUm_;
    else if (posUm > maxTravelUm_)
        posUm = maxTravelUm_;

    newPosition=(float)posUm/1000;
    MOT_SetChannel(serialNumber_, chNumber_-1);
    MOT_MoveAbsoluteEx(serialNumber_, newPosition, continuousFlag);
    curPosUm_=newPosition*1000;
    OnStagePositionChanged(curPosUm_);

    tmpMessage << "SetPositionUm:" <<posUm << " continuousFlag:" << continuousFlag;
    LogIt();

    return DEVICE_OK;
}


/**
 * Send Home command to the stage
 * If stage was already Homed, this command has no effect.
 * If not, then the zero sequence will be initiated.
 */

int ThorlabsMFF::Home()
{
    int ret = DEVICE_OK;

    if (homed_)
        return ret;

    if (minTravelUm_ == (pfMinPos * 1000) && maxTravelUm_ == (pfMaxPos * 1000))
    {
        //no software limits, true homing.
        ret = MOT_MoveHome(serialNumber_, 1);
    }
    else
    {
        //software limits, careful, go to lower limit.
        MOT_SetChannel(serialNumber_, chNumber_-1);
        ret = SetPositionUm(minTravelUm_);
    }

    if (ret == DEVICE_OK)
        homed_ = true;

    return ret;
}

int ThorlabsMFF::GetVelParam(double& vel)
{
    MOT_SetChannel(serialNumber_, chNumber_-1);
    MOT_GetVelParams(serialNumber_, &pfMinVel, &pfAccn, &pfMaxVel);
    vel=(double) pfMaxVel;

    return DEVICE_OK;
}

int ThorlabsMFF::SetVelParam(double vel)
{
    MOT_SetChannel(serialNumber_, chNumber_-1);
    MOT_SetVelParams(serialNumber_, 0.0 , pfAccn, (float)vel);

    return DEVICE_OK;
}

///////////////////////////////////////////////////////////////////////////////
// Action handlers
///////////////////////////////////////////////////////////////////////////////

int ThorlabsMFF::OnSerialNumber(MM::PropertyBase* pProp, MM::ActionType eAct)
{
    if (eAct == MM::BeforeGet)
    {
        pProp->Set(serialNumber_);
    }
    else if (eAct == MM::AfterSet)
    {
        if (initialized_)
        {
            long serialNumber;
            pProp->Get(serialNumber);
            serialNumber_ = serialNumber;

        }

        pProp->Get(serialNumber_);
    }

    if (aptInitialized == true)
        InitHWDevice(serialNumber_);

    tmpMessage << "Serial number set to " << serialNumber_;
    LogIt();

    return DEVICE_OK;
}

int ThorlabsMFF::OnChannelNumber(MM::PropertyBase* pProp, MM::ActionType eAct)
{
    if (eAct == MM::BeforeGet)
    {
        pProp->Set(chNumber_);
    }
    else if (eAct == MM::AfterSet)
    {
        if (initialized_)
        {
            long chNumber;
            pProp->Get(chNumber);
            chNumber_ = chNumber;

        }

        pProp->Get(chNumber_);
    }

    tmpMessage << "Channel number set to " << chNumber_;
    LogIt();

    return DEVICE_OK;
}

int ThorlabsMFF::OnMinPosUm(MM::PropertyBase* pProp, MM::ActionType eAct)
{
    if (eAct == MM::BeforeGet)
    {
        if (minTravelUm_ > pfMaxPos * 1000)
            minTravelUm_ = pfMaxPos * 1000;
        else if (minTravelUm_ < pfMinPos * 1000)
            minTravelUm_ = pfMinPos * 1000;
        pProp->Set(minTravelUm_);
    }
    else if (eAct == MM::AfterSet)
    {
        if (initialized_)
        {
            double minTravelUm;
            pProp->Get(minTravelUm);
            minTravelUm_ = minTravelUm;
        }

        pProp->Get(minTravelUm_);
        if (minTravelUm_ > pfMaxPos * 1000)
            minTravelUm_ = pfMaxPos * 1000;
        else if (minTravelUm_ < pfMinPos * 1000)
            minTravelUm_ = pfMinPos * 1000;
    }

    tmpMessage << "minTravelUm_ set to " << minTravelUm_ << " pfMinPos:" << pfMinPos << " pfMaxPos:" << pfMaxPos;
    LogIt();

    return DEVICE_OK;
}

int ThorlabsMFF::OnMaxPosUm(MM::PropertyBase* pProp, MM::ActionType eAct)
{
    if (eAct == MM::BeforeGet)
    {
        if (maxTravelUm_ > pfMaxPos * 1000)
            maxTravelUm_ = pfMaxPos * 1000;
        else if (maxTravelUm_ < pfMinPos * 1000)
            maxTravelUm_ = pfMinPos * 1000;

        pProp->Set(maxTravelUm_);
    }
    else if (eAct == MM::AfterSet)
    {
        if (initialized_)
        {
            double maxTravelUm;
            pProp->Get(maxTravelUm);
            maxTravelUm_ = maxTravelUm;
        }

        pProp->Get(maxTravelUm_);

        if (maxTravelUm_ > pfMaxPos * 1000)
            maxTravelUm_ = pfMaxPos * 1000;
        else if (maxTravelUm_ < pfMinPos * 1000)
            maxTravelUm_ = pfMinPos * 1000;
    }

    tmpMessage << "maxTravelUm_ set to " << maxTravelUm_ << " pfMinPos:" << pfMinPos << " pfMaxPos:" << pfMaxPos;
    LogIt();

    return DEVICE_OK;
}

int ThorlabsMFF::OnPosition(MM::PropertyBase* pProp, MM::ActionType eAct)
{
    if (eAct == MM::BeforeGet)
    {
        double pos;
        int ret = GetPositionUm(pos);
        if (ret != DEVICE_OK)
            return ret;

        pProp->Set(pos);
    }
    else if (eAct == MM::AfterSet)
    {
        double pos;
        pProp->Get(pos);
        int ret = SetPositionUm(pos);
        if (ret != DEVICE_OK)
            return ret;
    }

    return DEVICE_OK;
}

int ThorlabsMFF::OnVelocity(MM::PropertyBase* pProp, MM::ActionType eAct)
{
    if (eAct == MM::BeforeGet)
    {
        double vel;

        int ret = GetVelParam(vel);
        if (ret != DEVICE_OK)
            return ret;

        pProp->Set(vel);
    }
    else if (eAct == MM::AfterSet)
    {
        double vel;
        pProp->Get(vel);
        int ret = SetVelParam(vel);
        if (ret != DEVICE_OK)
            return ret;
    }

    return DEVICE_OK;
}

int ThorlabsMFF::OnHome(MM::PropertyBase* pProp, MM::ActionType eAct)
{
    if (eAct == MM::BeforeGet)
    {
        pProp->Set((long)homed_);
    }
    else if (eAct == MM::AfterSet)
    {
        long homed;
        pProp->Get(homed);

        int ret = Home();
        if (ret != DEVICE_OK)
            return ret;
    }
    return DEVICE_OK;
}
// vim: set autoindent tabstop=4 softtabstop=4 shiftwidth=4 expandtab textwidth=78:
