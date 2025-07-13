// src/App.tsx
import {
    ChevronLeftIcon,
    ChevronRightIcon,
    LinkIcon,
    CalendarIcon,
    ClockIcon,
    UserIcon,
    ArrowsRightLeftIcon,
    DocumentTextIcon,
    // RouteIcon,
    ChartBarIcon,
    Cog6ToothIcon,
    QuestionMarkCircleIcon,
    CalendarDaysIcon
} from '@heroicons/react/24/outline';

function App() {
    return (
        <div className="flex h-screen bg-gray-100">
            {/* Sidebar */}
            <aside className="w-64 bg-white border-r flex flex-col">
                {/* Header */}
                <div className="flex items-center p-4 border-b">
                    <span className="text-blue-600 text-2xl font-bold">Calendly</span>
                    <button className="ml-auto text-gray-500 hover:text-gray-700">
                        <ChevronLeftIcon className="h-6 w-6" />
                    </button>
                </div>
                {/* Create */}
                <div className="p-4">
                    <button className="w-full flex items-center justify-center py-2 px-4 border border-blue-600 text-blue-600 rounded hover:bg-blue-50">
                        + Create
                    </button>
                </div>
                {/* Navigation */}
                <nav className="flex-1 overflow-y-auto">
                    <ul>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <LinkIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Event types</span>
                        </li>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <CalendarIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Meetings</span>
                        </li>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <ClockIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Availability</span>
                        </li>
                        <li className="mt-4 px-4 py-2 hover:bg-gray-100 flex items-center">
                            <UserIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Contacts</span>
                        </li>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <ArrowsRightLeftIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Workflows</span>
                        </li>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <DocumentTextIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Integrations & apps</span>
                        </li>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <span className="text-gray-800">Routing</span>
                        </li>
                    </ul>
                </nav>
                {/* Footer */}
                <div className="p-4 border-t">
                    <ul>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <ChartBarIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Analytics</span>
                        </li>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <Cog6ToothIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Admin center</span>
                        </li>
                        <li className="px-4 py-2 hover:bg-gray-100 flex items-center">
                            <QuestionMarkCircleIcon className="h-5 w-5 text-gray-600 mr-3" />
                            <span className="text-gray-800">Help</span>
                        </li>
                    </ul>
                </div>
            </aside>

            {/* Main content */}
            <main className="flex-1 p-8">
                <h1 className="text-2xl font-semibold text-gray-800 mb-6">Event types</h1>
                <div className="flex flex-col items-center justify-center h-full space-y-4">
                    <div className="bg-blue-100 p-6 rounded-full">
                        <CalendarDaysIcon className="h-12 w-12 text-blue-600" />
                    </div>
                    <h2 className="text-xl font-medium text-gray-800">
                        Create scheduling links with event types
                    </h2>
                    <p className="text-gray-600 text-center max-w-md">
                        Create event types for meetings you'll want to schedule regularly, like product demos, customer calls, office hours, and more.
                    </p>
                    <button className="mt-4 inline-flex items-center text-blue-600 hover:underline">
                        Learn more
                        <ChevronRightIcon className="h-5 w-5 ml-1" />
                    </button>
                    <button className="mt-6 px-5 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                        + New event type
                    </button>
                </div>
            </main>
        </div>
    );
}

export default App;
