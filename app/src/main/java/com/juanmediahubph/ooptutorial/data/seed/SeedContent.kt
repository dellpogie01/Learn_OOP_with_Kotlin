package com.juanmediahubph.ooptutorial.data.seed

import androidx.room.withTransaction
import com.juanmediahubph.ooptutorial.data.local.AppSettingEntity
import com.juanmediahubph.ooptutorial.data.local.LessonEntity
import com.juanmediahubph.ooptutorial.data.local.ModuleEntity
import com.juanmediahubph.ooptutorial.data.local.OopDatabase
import com.juanmediahubph.ooptutorial.data.local.QuestionEntity
import com.juanmediahubph.ooptutorial.data.local.QuestionOptionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class LessonSeed(
    val id: String,
    val moduleId: String,
    val title: String,
    val difficulty: String,
    val objectives: String,
    val readingMinutes: Int,
    val explanation: String,
    val example: String,
    val code: String,
    val codeExplanation: String,
    val commonMistakes: String,
    val takeaway: String
)

data class QuestionSeed(
    val id: String,
    val moduleId: String,
    val lessonId: String,
    val difficulty: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val code: String? = null,
    val misconceptionTag: String? = null
)

object SeedContent {
    const val VERSION = "1"

    val modules = listOf(
        ModuleEntity("m01", "Introduction to OOP", "Compare procedural steps with collaborating objects.", "Beginner", 1),
        ModuleEntity("m02", "Classes and objects", "Model reusable types and create concrete instances.", "Beginner", 2),
        ModuleEntity("m03", "Properties and constructors", "Initialize state and define useful object behavior.", "Beginner", 3),
        ModuleEntity("m04", "Encapsulation", "Protect invariants with controlled visibility.", "Beginner", 4),
        ModuleEntity("m05", "Abstraction", "Expose essentials while hiding implementation detail.", "Intermediate", 5),
        ModuleEntity("m06", "Inheritance", "Extend open types and override behavior safely.", "Intermediate", 6),
        ModuleEntity("m07", "Polymorphism", "Use common contracts with runtime dispatch.", "Intermediate", 7),
        ModuleEntity("m08", "Interfaces", "Describe capabilities independent of class ancestry.", "Intermediate", 8),
        ModuleEntity("m09", "Object relationships", "Choose association, aggregation, composition, or delegation.", "Intermediate", 9),
        ModuleEntity("m10", "Kotlin object tools", "Use data classes, enums, objects, and companions.", "Beginner", 10),
        ModuleEntity("m11", "SOLID principles", "Design focused, extensible, substitutable components.", "Advanced", 11),
        ModuleEntity("m12", "A real-world application", "Combine OOP ideas in a small offline reading app.", "Intermediate", 12)
    )

    val lessons = listOf(
        lesson("m01", "01", "Thinking in objects", "Beginner",
            "Identify objects as state plus behavior; distinguish an object model from a sequence of instructions.",
            "A library checkout can be written as steps that move strings between lists. An object-oriented design gives a Book its title and availability, and gives a Library the operation that coordinates checkout. The benefit is not that every value needs a class; it is that related state and rules live together.",
            "A thermostat has a target temperature (state) and a setTarget operation (behavior). A client asks the thermostat to change rather than editing an unrelated global variable.",
            "class Thermostat(private var targetC: Int) {\n    fun setTarget(value: Int) {\n        require(value in 5..35)\n        targetC = value\n    }\n\n    fun target(): Int = targetC\n}",
            "The constructor initializes private state. setTarget keeps the range rule close to that state, while callers use a small public API.",
            "Do not claim OOP always uses less code. Avoid making a class for every primitive value without a clear responsibility.",
            "An object combines meaningful state and behavior behind a useful boundary."),
        lesson("m01", "02", "Procedural and object-oriented approaches", "Beginner",
            "Compare where data and operations live; choose a style that fits the problem.",
            "In procedural code, functions operate on data passed to them and the program's flow is central. In OOP, collaborating instances own state and expose operations. Both styles can be clear; a small one-off calculation may be simpler as a function, while a long-lived domain entity benefits from protecting its rules.",
            "For a shopping cart, procedural code might pass a mutable list to add, remove, and total functions. An object-oriented Cart can own its item list and calculate the total consistently.",
            "data class CartItem(val name: String, val price: Int)\n\nclass Cart {\n    private val items = mutableListOf<CartItem>()\n    fun add(item: CartItem) { items += item }\n    fun total(): Int = items.sumOf { it.price }\n}",
            "Cart owns the collection; callers do not receive its mutable list. The data class is a simple value record, not a replacement for every domain object.",
            "Do not confuse OOP with using inheritance everywhere. Do not hide simple transformations inside stateful objects unnecessarily.",
            "Choose the style that makes data ownership and rules easiest to understand."),
        lesson("m02", "01", "A class is a blueprint", "Beginner",
            "Read a Kotlin class declaration; distinguish a type from its instances.",
            "A class declares the shape and behavior shared by instances. Declaring a class does not create a particular learner; constructing it does. Each instance has its own identity and values.",
            "A Student class is a form; Maya and Luis are separate records made from it.",
            "class Student(val name: String, var year: Int) {\n    fun advance() { year += 1 }\n}\n\nval maya = Student(\"Maya\", 1)\nval luis = Student(\"Luis\", 2)",
            "Both variables refer to Student objects. Calling maya.advance() changes Maya's year but not Luis's.",
            "Do not call the class itself an object instance. Prefer val for state that should not be reassigned.",
            "A class defines a type; construction creates an instance with independent state."),
        lesson("m02", "02", "Creating and using objects", "Beginner",
            "Construct instances and invoke properties and methods using Kotlin syntax.",
            "Kotlin calls a primary constructor by writing the class name and arguments; it does not use a new keyword. A property access reads a value and a method call asks an instance to perform behavior.",
            "Two lamps can share a Lamp class while keeping separate on/off state.",
            "class Lamp(var isOn: Boolean = false) {\n    fun toggle() { isOn = !isOn }\n}\n\nval deskLamp = Lamp()\nval floorLamp = Lamp(true)\ndeskLamp.toggle()",
            "The default constructor argument lets the first lamp start off. Toggling deskLamp has no effect on floorLamp.",
            "Do not use Java's new keyword in Kotlin. Do not make mutable state global when each instance needs its own value.",
            "Construct each object with its own initial data, then use its public behavior."),
        lesson("m03", "01", "Properties and methods", "Beginner",
            "Define properties with val or var; use methods to express object behavior.",
            "A Kotlin property provides a declared value and may have accessors; val cannot be reassigned after initialization, while var can. Methods are functions declared on a class and can coordinate changes to that class's state.",
            "A bank account has a fixed account number and a changing balance. A deposit operation can enforce a positive amount.",
            "class Account(val number: String, private var cents: Long = 0) {\n    fun deposit(amount: Long) {\n        require(amount > 0)\n        cents += amount\n    }\n    fun balance(): Long = cents\n}",
            "number is a read-only property. cents is private mutable state; the method validates changes before applying them.",
            "Do not expose a writable balance if callers could bypass validation. Remember val prevents reassignment, not mutation of a referenced mutable object.",
            "Use properties for state and methods for meaningful, rule-aware behavior."),
        lesson("m03", "02", "Constructors and init blocks", "Beginner",
            "Initialize required state in a primary constructor and validate it during construction.",
            "A primary constructor appears after the class name. An init block runs during instance construction, in source order with property initializers. Secondary constructors can provide alternative ways to create an instance, but should delegate to the primary constructor.",
            "A labeled box should reject a blank label immediately rather than becoming invalid and failing much later.",
            "class Box(val label: String, val capacity: Int) {\n    init {\n        require(label.isNotBlank())\n        require(capacity > 0)\n    }\n}",
            "Kotlin runs the init checks for every construction path that delegates to this primary constructor.",
            "Do not put expensive unrelated work in init. Do not assume a property declared in the body initializes before an earlier init block.",
            "Constructors establish valid initial state; init blocks are useful for constructor invariants."),
        lesson("m04", "01", "Encapsulation and invariants", "Beginner",
            "Keep data and the rules that protect it together; expose a small safe API.",
            "Encapsulation means controlling access to an object's representation so its invariants remain true. It is not simply hiding every field. The class chooses which operations are public and validates changes at the boundary.",
            "A playlist can prevent a negative track index by exposing next() rather than allowing callers to edit its internal cursor.",
            "class Playlist(private val tracks: List<String>) {\n    private var index = 0\n    fun current(): String? = tracks.getOrNull(index)\n    fun next(): String? {\n        if (index + 1 < tracks.size) index++\n        return current()\n    }\n}",
            "The cursor cannot be changed by callers. The public methods keep it within the playlist bounds.",
            "Do not mistake private fields alone for good encapsulation if public methods still allow invalid state. Avoid exposing mutable collections directly.",
            "Encapsulation protects valid state through intentional operations."),
        lesson("m04", "02", "Kotlin visibility modifiers", "Beginner",
            "Explain public, private, protected, and internal visibility in Kotlin.",
            "public is the default and is visible wherever the declaration is accessible. private limits access to its declaring scope (or class/file as appropriate). protected is available in a class and its subclasses. internal limits access to the same module. Choose the narrowest visibility that supports real callers.",
            "A module's internal parser can stay internal; a public screen API can expose only the operations other app modules need.",
            "class Token(private val raw: String) {\n    fun masked(): String = \"*\".repeat(raw.length)\n    protected fun rawLength(): Int = raw.length\n}\n\ninternal fun normalizeName(value: String) = value.trim()",
            "Callers can request a masked value but cannot read raw directly. Subclasses can use protected behavior; normalizeName is module-scoped.",
            "protected does not mean package-private. internal means module visibility, not a Java package boundary.",
            "Visibility is part of an API contract; make exposure intentional."),
        lesson("m05", "01", "Abstraction: expose what matters", "Intermediate",
            "Separate the essential contract from implementation details; contrast abstraction with encapsulation.",
            "Abstraction describes a useful simplified contract: what a caller can ask a service to do. Encapsulation controls access to the state and implementation behind that boundary. The ideas support each other but are not synonyms.",
            "A coffee machine offers brew(size). A customer need not manage heater timing and water-pump pulses.",
            "interface CoffeeMaker {\n    fun brew(size: String): String\n}\n\nclass SimpleCoffeeMaker : CoffeeMaker {\n    override fun brew(size: String) = \"Coffee: \$size\"\n}",
            "CoffeeMaker is the abstraction the caller depends on. SimpleCoffeeMaker encapsulates how the result is produced.",
            "Do not confuse an abstract contract with a guarantee that every implementation is correct. Do not call private fields the whole of abstraction.",
            "Abstraction gives callers a stable, relevant view; encapsulation guards the details."),
        lesson("m05", "02", "Abstract classes", "Intermediate",
            "Use abstract classes for shared state or implementation with required subclass behavior.",
            "An abstract class cannot be instantiated directly. It may define constructors, stored properties, implemented functions, and abstract members that concrete subclasses must implement. Kotlin classes are final by default, but an abstract class is designed for inheritance.",
            "A Shape can share an identifier while requiring each concrete shape to calculate its own area.",
            "abstract class Shape(val name: String) {\n    abstract fun area(): Double\n    fun label(): String = \"\$name: \${area()}\"\n}\n\nclass Circle(private val radius: Double) : Shape(\"Circle\") {\n    override fun area() = Math.PI * radius * radius\n}",
            "Shape supplies label() and demands area(). Circle provides the implementation; clients can use either through Shape.",
            "Do not create an abstract class when an interface alone expresses the capability. Do not forget to implement abstract members.",
            "Use an abstract class when related types genuinely share base state or behavior."),
        lesson("m06", "01", "Inheritance and open classes", "Intermediate",
            "Extend a Kotlin base class deliberately and understand final-by-default behavior.",
            "Kotlin classes and members are final unless marked open or abstract. Inheritance expresses an is-a relationship and lets a subtype reuse or specialize behavior. It also couples the subtype to base-class rules, so it should not be the default reuse technique.",
            "A SavingsAccount is an Account if it obeys the guarantees callers expect from every account.",
            "open class Account(val id: String) {\n    open fun monthlyFee(): Int = 0\n}\n\nclass StudentAccount(id: String) : Account(id) {\n    override fun monthlyFee(): Int = 0\n}",
            "Account is open so it can be subclassed, and monthlyFee is open so it can be overridden. StudentAccount uses override explicitly.",
            "Do not try to extend a final class. Do not use inheritance merely to borrow a helper function.",
            "Kotlin requires an explicit design decision before a class can be inherited."),
        lesson("m06", "02", "Overriding is not overloading", "Intermediate",
            "Distinguish runtime overriding from same-name overloads selected by parameters.",
            "Overriding replaces an inherited open member with a compatible implementation in a subtype; dynamic dispatch chooses it at runtime. Overloading declares multiple functions with the same name but different parameter lists; the compiler chooses based on the call's arguments. They solve different problems.",
            "A printer may overload print(text) and print(number). A ColorPrinter may override an inherited print(text) behavior.",
            "open class Printer {\n    open fun print(text: String) = \"Text: \$text\"\n}\nclass ColorPrinter : Printer() {\n    override fun print(text: String) = \"Color: \$text\"\n    fun print(number: Int) = \"Number: \$number\"\n}",
            "The String member overrides Printer.print(String); the Int member is an overload, not an override.",
            "Do not add override to a new overload. Do not assume overload selection depends on the runtime subtype.",
            "Override changes inherited behavior; overload adds a distinct parameter signature."),
        lesson("m07", "01", "Polymorphism and dynamic dispatch", "Intermediate",
            "Call subtype-specific behavior through a shared supertype safely.",
            "Polymorphism lets code work with values of a common type while each actual object provides its own implementation. For overridden members, Kotlin dispatches to the runtime implementation. This allows callers to depend on a contract rather than a chain of type checks.",
            "A drawing screen can render circles and rectangles through one Shape interface.",
            "interface Shape {\n    fun area(): Double\n}\nclass Square(private val side: Double) : Shape {\n    override fun area() = side * side\n}\nval shapes: List<Shape> = listOf(Square(3.0))\nval total = shapes.sumOf { it.area() }",
            "The list stores Shape references; calling area dispatches to Square's implementation.",
            "Do not assume the declared variable type changes the object's implementation. Avoid unsafe casts when a shared contract is enough.",
            "Program to the shared contract and let the object's runtime type supply behavior."),
        lesson("m07", "02", "Dynamic dispatch in practice", "Intermediate",
            "Trace an overridden method call and identify the runtime receiver.",
            "When an open member is called through a base-typed reference, the implementation belonging to the runtime object is used. This is dynamic dispatch. A non-overridden final member is not dynamically replaced.",
            "A notification sender can receive EmailSender or SmsSender while the delivery loop stays unchanged.",
            "open class Sender {\n    open fun send(message: String) = \"Base: \$message\"\n}\nclass EmailSender : Sender() {\n    override fun send(message: String) = \"Email: \$message\"\n}\nfun deliver(sender: Sender) = sender.send(\"Hello\")",
            "Passing an EmailSender to deliver prints the EmailSender result because send is overridden and the object is an EmailSender.",
            "Do not confuse dynamic dispatch with overload resolution. Do not shadow a member and expect an override.",
            "Overridden member calls follow the runtime object's implementation."),
        lesson("m08", "01", "Interfaces describe capabilities", "Intermediate",
            "Declare a contract without forcing a class hierarchy.",
            "An interface describes operations or properties a class promises to support. A Kotlin class can implement more than one interface. Interfaces do not hold per-instance backing state for ordinary abstract properties, though they can provide default behavior.",
            "A document and a photo can both be Shareable without being the same kind of thing.",
            "interface Shareable {\n    fun shareText(): String\n}\nclass Note(private val text: String) : Shareable {\n    override fun shareText() = text\n}",
            "Note commits to the Shareable contract and can be passed to any function expecting Shareable.",
            "Do not instantiate an interface. Do not add a large unrelated interface that forces empty implementations.",
            "Interfaces model capabilities that unrelated classes can share."),
        lesson("m08", "02", "Implementing multiple interfaces", "Intermediate",
            "Implement multiple contracts and resolve a default-method conflict explicitly.",
            "A class can list multiple interfaces after a colon. If two interfaces provide implementations of the same signature, the class must override that member and may call a specific parent implementation with super<InterfaceName>.",
            "A smart speaker might be both Playable and VoiceControllable.",
            "interface Playable { fun stop() = \"Playback stopped\" }\ninterface VoiceControllable { fun stop() = \"Voice session stopped\" }\nclass Speaker : Playable, VoiceControllable {\n    override fun stop() = super<Playable>.stop()\n}",
            "Speaker satisfies both contracts and selects which default stop implementation to use.",
            "Do not omit conflict resolution when defaults collide. Prefer clear contracts over marker interfaces with no useful meaning.",
            "Multiple interfaces compose capabilities; the implementing class owns conflict decisions."),
        lesson("m09", "01", "Association, aggregation, composition", "Intermediate",
            "Describe object links and reason about ownership and lifetime.",
            "Association is a general relationship between objects. Aggregation is a whole-part relationship where parts can live independently of the whole. Composition is strong ownership where a part's lifetime is controlled by its owner. These are design interpretations; Kotlin syntax alone does not enforce them.",
            "A teacher is associated with a course. A team aggregates independently existing players. A document may compose its own private pages.",
            "data class Player(val name: String)\nclass Team(val players: List<Player>)\nclass Document {\n    private val pages = mutableListOf<String>()\n    fun addPage(text: String) { pages += text }\n}",
            "Team receives player objects that can exist elsewhere. Document controls its internal page collection.",
            "Do not claim aggregation and composition have special Kotlin keywords. Do not infer ownership only from a field type.",
            "Choose the relationship by who owns a part and how long it may live."),
        lesson("m09", "02", "Delegation and composition", "Intermediate",
            "Prefer object composition for reuse and use Kotlin's delegation syntax appropriately.",
            "Composition builds behavior from collaborators. Delegation forwards a contract's work to another object, reducing repetitive forwarding code. Inheritance reuses behavior through a subtype relationship; composition does not claim the containing type is the collaborator.",
            "A secure logger can add a prefix while delegating actual storage to another Logger.",
            "interface Logger { fun write(message: String) }\nclass MemoryLogger : Logger {\n    override fun write(message: String) { println(message) }\n}\nclass PrefixLogger(private val prefix: String, private val delegate: Logger) : Logger by delegate {\n    override fun write(message: String) = delegate.write(\"\$prefix: \$message\")\n}",
            "PrefixLogger forwards unoverridden Logger members and overrides write() to add a prefix before delegating the message.",
            "Do not call delegation inheritance. Do not use `by` when the wrapper must alter every delegated operation without an override.",
            "Composition creates a has-a relationship; delegation forwards a chosen contract."),
        lesson("m10", "01", "Data classes and enums", "Beginner",
            "Choose data classes for value records and enums for a fixed set of named states.",
            "A data class generates value-oriented methods such as equals, hashCode, copy, and component functions from its primary-constructor properties. An enum class represents a finite set of named constants. Neither should be used as a shortcut for unrelated mutable behavior.",
            "A flashcard is a value record; its review state can use a small finite enum.",
            "data class Flashcard(val prompt: String, val answer: String)\nenum class ReviewState { NEW, LEARNING, MASTERED }\nval first = Flashcard(\"OOP?\", \"Objects with responsibilities\")\nval revised = first.copy(answer = \"Collaborating objects\")",
            "Equality for Flashcard compares its constructor values. ReviewState restricts a state to known alternatives.",
            "Do not put mutable collections into a data class and assume they are deeply immutable. Enum entries are values, not strings.",
            "Use data classes for values and enums for a closed, named set of choices."),
        lesson("m10", "02", "Object declarations and companions", "Beginner",
            "Use object for one shared instance and companion objects for class-related members.",
            "An object declaration creates a singleton instance. A companion object is a singleton associated with a class and is a conventional place for factories or constants; it is not truly a Java-style static object. Use top-level declarations when class association adds no clarity.",
            "A parser registry may be one shared object; a Color factory belongs naturally with Color.",
            "object IdSource {\n    private var next = 1\n    fun nextId(): Int = next++\n}\nclass Color private constructor(val hex: String) {\n    companion object {\n        fun fromHex(value: String) = Color(value)\n    }\n}",
            "IdSource is one shared instance. Color.fromHex is accessed through the class and can call its private constructor.",
            "Do not use a singleton as a global mutable state bucket. Do not assume every utility belongs in a companion.",
            "Use singleton declarations sparingly; companions group class-related construction and constants."),
        lesson("m11", "01", "Single responsibility and open-closed", "Advanced",
            "Apply SRP and OCP by separating reasons to change and extending through contracts.",
            "Single Responsibility Principle (SRP) asks a module to have one coherent reason to change. Open-Closed Principle (OCP) encourages extending behavior without repeatedly editing stable core logic. Neither means one tiny class per line; the design should keep related changes together.",
            "A receipt formatter should not also persist purchases. A new formatter can implement a shared contract rather than adding another conditional to the checkout flow.",
            "data class Receipt(val total: Int)\ninterface ReceiptFormatter { fun format(receipt: Receipt): String }\nclass PlainFormatter : ReceiptFormatter {\n    override fun format(receipt: Receipt) = \"Total: \${receipt.total}\"\n}",
            "ReceiptFormatter gives the checkout code an extension point. Persistence and formatting can change independently.",
            "Do not interpret SRP as exactly one method per class. Do not introduce abstractions before there is a real variation point.",
            "Keep cohesive responsibilities together and extend stable behavior through well-chosen contracts."),
        lesson("m11", "02", "Liskov, interface segregation, dependency inversion", "Advanced",
            "Recognize substitutability, focused interfaces, and dependencies on abstractions.",
            "Liskov Substitution Principle (LSP) requires a subtype to honor the expectations of its base contract. Interface Segregation Principle (ISP) favors focused contracts over forcing clients to depend on unused methods. Dependency Inversion Principle (DIP) places policy behind abstractions so high-level code does not depend on low-level details.",
            "A read-only source should not pretend it can save. A report service can depend on a small ReportStore interface rather than a specific disk class.",
            "interface ReportStore { fun load(): String }\nclass ReportService(private val store: ReportStore) {\n    fun report() = store.load().uppercase()\n}",
            "ReportService depends on the capability it needs. A memory or file implementation can provide it without changing the service.",
            "Do not add a save method to a read-only client contract. Do not override a method by strengthening its preconditions unexpectedly.",
            "Honor contracts and keep dependencies pointed toward the policy-facing abstraction."),
        lesson("m12", "01", "Designing an offline reading app", "Intermediate",
            "Identify domain objects, responsibilities, and persistence boundaries in a small app.",
            "A reading app can keep lesson content, progress, and UI concerns separate. A Lesson is bundled content, a Progress record tracks a learner's completion, and a repository coordinates Room access. The screen renders state rather than writing SQL or owning grading rules.",
            "The app remains useful without a network because bundled lessons are imported into a local SQLite database.",
            "data class Lesson(val id: String, val title: String, val body: String)\ninterface LessonRepository {\n    suspend fun find(id: String): Lesson?\n    suspend fun markComplete(id: String)\n}",
            "The model represents content; the repository contract hides its storage. A Room-backed implementation can satisfy the same contract.",
            "Do not put Android views in domain models. Do not let a UI screen bypass repository rules to update tables directly.",
            "Separate bundled content, persisted learner state, and presentation responsibilities."),
        lesson("m12", "02", "Connecting the application layers", "Intermediate",
            "Trace user actions through a ViewModel, repository, and Room database.",
            "A single user action should have a clear route: Compose sends an event to a ViewModel, the ViewModel calls a repository, and the repository uses Room on a coroutine dispatcher. StateFlow carries observable state back to the UI. A transaction protects multi-row changes.",
            "When a learner completes a lesson, the app records a timestamp and updates its displayed progress from database-backed state.",
            "class LessonViewModel(private val repository: LessonRepository) {\n    suspend fun complete(id: String) = repository.markComplete(id)\n}\n// Compose observes state and calls complete() from a coroutine.",
            "The ViewModel coordinates the action but does not contain SQL. The UI observes state and remains lifecycle-aware.",
            "Do not block the main thread with database work. Do not duplicate the database as a second source of truth in a screen.",
            "Keep rendering, coordination, and storage responsibilities distinct.")
    )

    private fun lesson(
        module: String, suffix: String, title: String, difficulty: String, objectives: String,
        explanation: String, example: String, code: String, codeExplanation: String,
        commonMistakes: String, takeaway: String
    ) = LessonSeed("$module-l$suffix", module, title, difficulty, objectives, 5, explanation, example, code, codeExplanation, commonMistakes, takeaway)

    private fun q(module: String, n: Int, prompt: String, a: String, b: String, c: String, d: String, correct: Int, explanation: String, code: String? = null, tag: String? = null) =
        QuestionSeed(String.format(Locale.ROOT, "%s-q%02d", module, n), module, "$module-l${if (n % 2 == 0) "02" else "01"}", when (n % 3) { 0 -> "Advanced"; 1 -> "Beginner"; else -> "Intermediate" }, prompt, listOf(a, b, c, d), correct, explanation, code, tag)

    val questions = listOf(
        q("m01", 1, "Which design keeps checkout rules beside the library state they protect?", "A Library object with a checkout operation", "A global Boolean changed by any screen", "A list of unrelated printing functions", "A constant containing every book title", 0, "The Library can own its catalog and enforce checkout rules at one boundary."),
        q("m01", 2, "What is the main difference between procedural and object-oriented organization?", "OOP cannot use functions", "Procedural code centers flow; OOP groups state and behavior around collaborating objects", "Procedural programs always use less memory", "OOP requires every value to be mutable", 1, "The difference is primarily organization, not a universal performance or syntax rule."),
        q("m01", 3, "A thermostat rejects temperatures outside its allowed range. Where is that rule best placed?", "In every screen that edits the temperature", "In a comment beside the database", "In the operation that changes thermostat state", "In a random number generator", 2, "Keeping validation at the state-owning operation prevents callers from bypassing the invariant."),
        q("m01", 4, "Which is a reasonable reason to use a plain function instead of a stateful class?", "The task is a simple stateless transformation", "The function must own a long-lived mutable cursor", "All OOP designs prohibit functions", "A function can guarantee shared state automatically", 0, "A pure transformation is often clearer as a function; use objects when ownership or collaboration helps."),
        q("m01", 5, "What does encapsulation contribute to a checkout design?", "It makes every property public", "It removes all data", "It lets one object protect state through a small API", "It forces remote networking", 2, "Encapsulation controls access so domain rules stay consistent."),
        q("m01", 6, "A design creates one class per integer without extra rules. What is the likely concern?", "OOP always requires it", "It adds ceremony without a clear modeling benefit", "Kotlin cannot store integers", "Integers are already singleton objects", 1, "Classes should express useful concepts, not wrap every primitive by default."),
        q("m01", 7, "In `val result = cart.total()`, what is `cart` most likely?", "A class declaration", "A concrete Cart instance", "A package name", "A constructor parameter type only", 1, "The variable refers to an object on which behavior is invoked."),
        q("m01", 8, "Why might a shopping Cart own its item collection?", "To prevent total calculation from being consistent", "To hide all behavior", "To keep collection changes and cart rules together", "To make items impossible to test", 2, "Ownership lets the Cart maintain coherent state and behavior."),
        q("m01", 9, "Which statement about OOP is accurate?", "It is always superior to procedural code", "It requires inheritance for all reuse", "It is one useful way to organize responsibilities", "It cannot be combined with functional code", 2, "Different styles can coexist; choose based on clarity."),
        q("m01", 10, "What is a domain object in the library example?", "An object representing a concept such as Book or Library", "A screen pixel", "A Gradle plugin", "A compiler warning", 0, "Domain objects model concepts and rules in the problem area."),
        q("m01", 11, "A function mutates a list passed from anywhere in the app. What risk increases?", "Rules may be scattered and state changes hard to trace", "The list becomes immutable", "The compiler adds Room automatically", "The function becomes an interface", 0, "Unclear ownership makes invariants harder to protect."),
        q("m01", 12, "Which takeaway best summarizes object-oriented thinking?", "Replace every function with a class", "Model useful responsibilities as objects that collaborate", "Store all state in globals", "Avoid data and behavior", 1, "The goal is clear responsibility and collaboration, not maximal class count."),

        q("m02", 1, "What does a class declaration primarily define?", "A reusable type's structure and behavior", "One mandatory global object", "A database transaction", "A running coroutine", 0, "A class defines a type; instances are created separately."),
        q("m02", 2, "In Kotlin, how is an instance of `Student` created?", "new Student()", "Student()", "instance Student", "make Student", 1, "Kotlin constructs an instance by calling the class constructor without `new`."),
        q("m02", 3, "Two Lamp instances each have `isOn`. What does this normally mean?", "Both share one automatic value", "Each has its own state", "Only the class can be called", "The values are compile-time constants", 1, "Instance properties belong to each constructed object."),
        q("m02", 4, "Which action changes only `deskLamp` in the example?", "Calling deskLamp.toggle()", "Changing the Lamp class name", "Calling a top-level compiler", "Creating another Lamp type", 0, "The method updates the receiver instance's own state."),
        q("m02", 5, "Which statement is true about constructing an object?", "It creates a concrete instance from a class", "It makes the class abstract", "It deletes the constructor", "It always writes a file", 0, "Construction initializes a new instance."),
        q("m02", 6, "Why can two objects of the same class differ?", "Their instance properties may hold different values", "They must have different class definitions", "One is always a singleton", "Kotlin copies source code per object", 0, "Instances share a type but can have independent state."),
        q("m02", 7, "How many separate Student instances are created by this code?", "One", "Two", "Three", "None", 1, "Each constructor call creates a distinct object.", code = "val maya = Student(\"Maya\", 1)\nval luis = Student(\"Luis\", 2)"),
        q("m02", 8, "What is the role of a method on a class?", "Represent behavior associated with an instance or type", "Declare an Android permission", "Convert a class into a package", "Replace all properties", 0, "Methods provide operations that may use class state."),
        q("m02", 9, "When should a class be introduced?", "When a meaningful reusable type or responsibility improves clarity", "For every local variable", "Only when Android requires it", "Whenever a function returns text", 0, "Classes are useful modeling tools, not mandatory wrappers."),
        q("m02", 10, "A `Student` class has a `name` property. What is `name`?", "A value exposed by the Student type or instance", "A module identifier", "A constructor invocation", "An object relationship kind", 0, "Properties represent data associated with a class or instance."),
        q("m02", 11, "What happens when `maya.advance()` is called?", "The class declaration is deleted", "The method runs with maya as its receiver", "Every Student is necessarily modified", "A new package is created", 1, "The receiver is the particular object the call targets."),
        q("m02", 12, "How are a class and object related?", "A class defines a type; an object is an instance", "They are always synonyms", "An object is a constructor parameter", "A class exists only after a network request", 0, "This is the fundamental distinction."),

        q("m03", 1, "Which Kotlin property cannot be reassigned after initialization?", "var", "val", "open", "lateinit", 1, "`val` is a read-only reference after initialization."),
        q("m03", 2, "What does a primary constructor usually provide?", "Initial values needed to create an instance", "A guaranteed network connection", "A Room migration", "An override for every method", 0, "Constructor parameters supply initial state."),
        q("m03", 3, "When does an `init` block run?", "During construction of the instance", "Only when a property is read", "After the app is uninstalled", "When an interface is declared", 0, "Init blocks are part of object initialization."),
        q("m03", 4, "What happens when this constructor receives a zero capacity?", "It accepts the invalid value", "It throws an argument validation exception", "It changes capacity to one", "It creates no Box type", 1, "require fails immediately when its condition is false.", code = "class Box(val capacity: Int) {\n    init { require(capacity > 0) }\n}"),
        q("m03", 5, "What is true about `val items: MutableList<String>`?", "The reference cannot be reassigned, but the list may still mutate", "The list is deeply immutable", "The property is always private", "The list is automatically copied", 0, "Val controls reference reassignment, not the mutability of the referenced object."),
        q("m03", 6, "Which design best protects an account balance?", "Expose a public writable balance with no checks", "Make balance private and validate deposit operations", "Store balance in a comment", "Use an enum instead of a number", 1, "The class owns the balance and enforces legal changes."),
        q("m03", 7, "What is a property accessor?", "Code associated with reading or writing a property", "A class inheritance marker", "A Gradle task", "A database index only", 0, "Kotlin properties can define getters and setters."),
        q("m03", 8, "Why prefer `val` when reassignment is unnecessary?", "It communicates stable reference intent", "It automatically encrypts data", "It makes all nested state immutable", "It allows subclasses", 0, "Read-only declarations reduce accidental reassignment."),
        q("m03", 9, "A secondary constructor in Kotlin must normally do what?", "Delegate to another constructor", "Be called with `new`", "Disable the primary constructor", "Run only after garbage collection", 0, "Secondary constructors delegate using this(...)."),
        q("m03", 10, "Which operation is behavior rather than stored state?", "deposit(amount)", "account number", "balance value", "creation timestamp", 0, "A method expresses an action; the others are values."),
        q("m03", 11, "What can happen if a constructor accepts an invalid blank label without checking?", "An invalid object may circulate and fail later", "Kotlin repairs the label automatically", "The object becomes an interface", "The app compiles all labels", 0, "Validate invariants as early as construction."),
        q("m03", 12, "Which initialization claim is correct?", "Init blocks and property initializers run in source order", "Init blocks run only once for the whole class", "Constructors cannot have parameters", "All properties default to zero", 0, "Initialization order matters for instance state."),

        q("m04", 1, "What is encapsulation mainly intended to protect?", "An object's invariants and implementation boundary", "Every method from testing", "All code from compilation", "Only the app icon", 0, "Encapsulation controls how state may be accessed or changed."),
        q("m04", 2, "Which visibility is the default for Kotlin declarations?", "private", "protected", "public", "internal", 2, "Public is the default visibility."),
        q("m04", 3, "What does `internal` generally mean?", "Visible within the same Kotlin module", "Visible only in one object instance", "Visible only from subclasses", "Hidden from the whole module", 0, "Internal defines module-level visibility."),
        q("m04", 4, "Where can a protected class member be accessed?", "From its class and subclasses", "From every unrelated package only", "Only from XML", "From no code", 0, "Protected supports subclass access."),
        q("m04", 5, "Why is returning a mutable internal list risky?", "Callers can bypass the owner's validation", "It changes a class into a function", "It guarantees sorting", "It disables constructors", 0, "Direct mutation breaks ownership of invariants."),
        q("m04", 6, "How does `private var index` help Playlist?", "Only the class can directly mutate its cursor", "It makes the playlist public", "It creates a database row", "It prevents methods from reading it", 0, "Private state can only be modified through the exposed operations."),
        q("m04", 7, "Which statement distinguishes encapsulation from abstraction?", "Encapsulation controls access; abstraction presents a useful contract", "They are exactly identical", "Abstraction is a Kotlin visibility keyword", "Encapsulation means inheritance", 0, "They are related but describe different design concerns."),
        q("m04", 8, "What is a good visibility choice for an implementation helper used only by one class?", "private", "public", "internal always", "protected always", 0, "Narrow visibility reduces accidental API surface."),
        q("m04", 9, "Does `private` by itself guarantee a class is well encapsulated?", "No; public operations can still permit invalid state", "Yes, every class becomes correct", "Yes, it encrypts all fields", "No, private is not a Kotlin modifier", 0, "The API must also preserve invariants."),
        q("m04", 10, "What does `public fun masked()` offer in the Token example?", "A safe public operation without exposing raw directly", "A way to change the package", "An automatic subclass", "A writable backing field", 0, "The public API reveals only a derived safe value."),
        q("m04", 11, "Visibility should generally be chosen based on what?", "Which real callers need access", "Alphabetical ordering", "Number of files only", "A requirement that all members are public", 0, "Visibility is part of the API design."),
        q("m04", 12, "What does this next() return when index already points at the last track?", "The last track again", "Null", "The first track", "An exception from an invalid index", 0, "The index remains unchanged, so getOrNull safely returns the current last track.", code = "fun next(): String? {\n    if (index + 1 < tracks.size) index++\n    return tracks.getOrNull(index)\n}"),

        q("m05", 1, "What does abstraction emphasize?", "The relevant contract a caller needs", "All private implementation details", "A class's memory address", "Only constructor syntax", 0, "Abstraction presents a simplified useful view."),
        q("m05", 2, "How does encapsulation differ from abstraction?", "It controls access to representation and behavior", "It always means an abstract class", "It removes contracts", "It requires multiple inheritance", 0, "Encapsulation protects implementation boundaries."),
        q("m05", 3, "Can an abstract class be instantiated directly?", "Yes, with no subclass", "No", "Only from XML", "Only when final", 1, "Abstract classes are incomplete types and cannot be constructed directly."),
        q("m05", 4, "What may an abstract class contain?", "Shared state, implemented methods, and abstract members", "Only constants", "No constructor", "Only private functions", 0, "Abstract classes can mix shared implementation with required subclass behavior."),
        q("m05", 5, "Why define `Shape.area()` as abstract?", "Each concrete shape must provide its own area rule", "To prevent all subclasses", "To calculate every area identically", "To make area a property automatically", 0, "Different shape types implement the contract differently."),
        q("m05", 6, "When is an interface often preferable to an abstract class?", "When only a capability contract is needed without shared base state", "When the class must own one shared constructor state", "When no contract is needed", "When a class must be final", 0, "Interfaces express capabilities without committing to a single base class."),
        q("m05", 7, "What is a caller of CoffeeMaker intended to know?", "How to request brewing through the contract", "How pump timing is implemented", "The exact hardware wiring", "The private water level field", 0, "A caller depends on the offered operation, not implementation details."),
        q("m05", 8, "Which is an abstraction in the example?", "CoffeeMaker with brew(size)", "The heater's pulse timing", "A private counter", "A local compiler variable", 0, "The interface exposes the essential brewing operation."),
        q("m05", 9, "Which member must Circle implement to become concrete?", "label()", "area()", "name setter", "a constructor named Shape", 1, "Shape declares area abstract, so a concrete Circle must provide it.", code = "abstract class Shape(val name: String) {\n    abstract fun area(): Double\n    fun label() = name\n}"),
        q("m05", 10, "What should an abstraction usually avoid exposing?", "Irrelevant low-level implementation detail", "The operation callers need", "Its meaningful contract", "A stable result type", 0, "Abstraction keeps the client-facing view focused."),
        q("m05", 11, "Does using an interface guarantee every implementation behaves correctly?", "No; implementations must still honor the contract", "Yes, automatically", "Only if the interface is public", "Only on Android", 0, "A type contract does not prove semantic correctness."),
        q("m05", 12, "Why is `Shape.label()` a shared implemented method?", "It reuses behavior based on the abstract area contract", "It prevents Circle from calculating area", "It creates a second interface", "It overrides the constructor", 0, "The base class implements behavior that works for all concrete shapes."),

        q("m06", 1, "What is Kotlin's default class inheritance behavior?", "Classes are open by default", "Classes are final unless opened", "Classes cannot inherit", "All classes are abstract", 1, "Kotlin uses final-by-default semantics."),
        q("m06", 2, "What keyword permits a class to be subclassed?", "open", "static", "virtual", "extendable", 0, "A class must be marked open or abstract to be subclassed."),
        q("m06", 3, "What relationship should inheritance usually represent?", "A substitutable is-a relationship", "Any code reuse at all", "A database foreign key only", "An unrelated has-a relationship", 0, "Inheritance creates a subtype relationship."),
        q("m06", 4, "What does `override` do?", "Provides a subtype implementation of an inherited open member", "Creates an unrelated function overload", "Makes a class final", "Changes a variable to val", 0, "Override replaces inherited virtual behavior."),
        q("m06", 5, "What is overloading?", "Same function name with different parameter signatures", "Replacing an inherited member at runtime", "Changing a class's visibility", "A Room migration", 0, "Overloads are distinct signatures selected at compile time."),
        q("m06", 6, "Which call is an overload rather than an override?", "print(number: Int) alongside print(text: String)", "A subtype reimplementation of open print(String)", "A call through a base reference", "A constructor init block", 0, "The Int signature is a distinct overload."),
        q("m06", 7, "Why can excessive inheritance be harmful?", "Subtypes become coupled to base-class behavior and assumptions", "It makes functions impossible", "It prevents any testing", "It guarantees duplication", 0, "Base-class changes and contracts affect subclasses."),
        q("m06", 8, "Can StudentAccount override monthlyFee in this code?", "Yes, all members are open by default", "No, the member is final by default", "Yes, because the class is open", "Only by overloading it", 1, "Opening a class does not open its members; monthlyFee remains final.", code = "open class Account {\n    fun monthlyFee(): Int = 0\n}\nclass StudentAccount : Account() {\n    override fun monthlyFee() = 0\n}"),
        q("m06", 9, "What should a subtype preserve for safe use as its base type?", "The expectations and guarantees of the base contract", "A new unrelated method name", "Its source file position", "A private constructor only", 0, "Subtypes must remain substitutable."),
        q("m06", 10, "Which feature is an alternative to inheriting just to reuse a helper?", "Composition with a collaborator", "Changing val to var", "A larger package name", "A second primary constructor", 0, "A has-a collaborator avoids a false subtype relationship."),
        q("m06", 11, "What is required to call a superclass constructor in Kotlin?", "Pass arguments in the superclass list", "Use the Java new keyword", "Call override()", "Define a companion object", 0, "Subclass declarations invoke the base constructor with parentheses and arguments."),
        q("m06", 12, "Which statement is accurate?", "Overriding changes inherited behavior; overloading adds another signature", "Both words mean dispatch", "Overloading requires an open class", "Overriding is only for properties", 0, "The concepts have distinct compile-time and runtime roles."),

        q("m07", 1, "What does polymorphism allow a caller to do?", "Use different concrete objects through a common contract", "Avoid all types", "Store only integers", "Disable runtime dispatch", 0, "A shared type lets callers work without knowing each concrete class."),
        q("m07", 2, "What selects an overridden method implementation at runtime?", "The actual receiver object's type", "The source file name", "The list's length", "The method's comment", 0, "Dynamic dispatch follows the runtime object."),
        q("m07", 3, "In a `List<Shape>`, what happens when `shape.area()` is called?", "The concrete shape implementation is invoked", "The list chooses randomly", "The interface runs no behavior", "Every shape uses a static area", 0, "The call dispatches to the runtime implementation."),
        q("m07", 4, "Why use a common Shape contract in a drawing loop?", "It avoids branching on every concrete class", "It forces all shapes to have identical code", "It removes the need to calculate area", "It turns shapes into strings", 0, "The loop invokes the shared operation polymorphically."),
        q("m07", 5, "Is overload resolution usually based on runtime receiver implementation?", "No, overload choice is based on the compile-time call signature", "Yes, it is always dynamic", "Only for enums", "Only for private methods", 0, "Overload resolution and overriding dispatch are different."),
        q("m07", 6, "What string does deliver(EmailSender()) return?", "Base: Hello", "Email: Hello", "Hello", "It cannot compile", 1, "The overridden method is dynamically dispatched using the runtime EmailSender.", code = "open class Sender { open fun send(message: String) = \"Base: \$message\" }\nclass EmailSender : Sender() {\n    override fun send(message: String) = \"Email: \$message\"\n}\nfun deliver(sender: Sender) = sender.send(\"Hello\")"),
        q("m07", 7, "What is a benefit of programming to a contract?", "New implementations can fit without changing client logic", "Clients must use unsafe casts", "Implementations cannot vary", "Only one object can exist", 0, "The client depends on capability rather than a concrete class."),
        q("m07", 8, "Which member participates in overriding dispatch?", "An open or abstract member with an override", "A local variable", "A top-level constant", "A final member", 0, "Only overridable members can be replaced in subtypes."),
        q("m07", 9, "What does `sender.send()` use to choose implementation?", "The runtime type of sender's object", "The variable name", "The number of arguments only", "The app theme", 0, "The call uses dynamic dispatch for an overridden member."),
        q("m07", 10, "Why might a type check chain be a design smell?", "It may duplicate behavior that belongs behind a polymorphic contract", "It improves every abstraction automatically", "It prevents all errors", "It guarantees compile-time selection", 0, "A shared behavior often removes repeated type-specific branching."),
        q("m07", 11, "Does polymorphism mean every object has identical data?", "No; objects share a contract while their state and behavior can differ", "Yes, all objects are copies", "Only if they are abstract", "Only in Java", 0, "Common contract does not require identical implementation or state."),
        q("m07", 12, "What is dynamic dispatch?", "Runtime selection of an overridden implementation", "Compile-time selection of an overload", "Database row sorting", "A coroutine scheduler", 0, "It is the runtime method-selection behavior."),

        q("m08", 1, "What does a Kotlin interface primarily describe?", "A capability contract", "A concrete singleton", "An Android activity", "A database file", 0, "Interfaces define behavior or properties a class promises."),
        q("m08", 2, "Can a Kotlin class implement multiple interfaces?", "Yes", "No, exactly one", "Only if it is abstract", "Only if they are empty", 0, "Multiple interface contracts can be listed."),
        q("m08", 3, "Can an interface normally store per-instance backing state?", "Yes, any field with a backing store", "No; it cannot have backing fields for abstract properties", "Only with public vars", "Only in a companion", 1, "Interfaces can declare abstract properties or accessors with behavior, but do not own instance backing state."),
        q("m08", 4, "What must Note do after implementing Shareable?", "Implement its required shareText operation", "Create a new Activity", "Extend every class", "Declare a database", 0, "Concrete implementers fulfill the abstract contract."),
        q("m08", 5, "What if two interfaces provide same-signature default implementations?", "The class must resolve the conflict", "Kotlin chooses alphabetically", "Both run automatically", "The class becomes final", 0, "The implementation must explicitly choose or combine behavior."),
        q("m08", 6, "Which expression explicitly calls Playable's default stop?", "super<Playable>.stop()", "Playable.new.stop()", "this.Playable.stop()", "super.stop(Playable)", 0, "Kotlin uses qualified super syntax to disambiguate.", code = "interface Playable { fun stop() = \"Playback stopped\" }\ninterface VoiceControllable { fun stop() = \"Voice stopped\" }\nclass Speaker : Playable, VoiceControllable {\n    override fun stop() = super<Playable>.stop()\n}"),
        q("m08", 7, "Why can unrelated classes implement Shareable?", "The interface models a capability, not a shared class ancestry", "Interfaces require identical fields", "It automatically subclasses them", "The compiler merges their data", 0, "Capabilities can cross unrelated class hierarchies."),
        q("m08", 8, "Which interface design best supports ISP?", "Small focused contracts relevant to each client", "One huge interface every class implements", "Only interfaces with dozens of unused methods", "A contract that exposes private state", 0, "Focused interfaces avoid unnecessary client dependencies."),
        q("m08", 9, "Can an interface contain a default method implementation?", "Yes", "No, never", "Only Java interfaces can", "Only if it has a backing field", 0, "Kotlin interfaces can provide default behavior."),
        q("m08", 10, "What is a good reason to introduce an interface?", "Several implementations need a stable capability contract", "Every data class requires one", "To make a class instantiable", "To avoid naming functions", 0, "Use an interface when it expresses a useful boundary."),
        q("m08", 11, "What does `override fun shareText()` communicate?", "The class fulfills or specializes an inherited contract member", "It overloads a constructor", "It makes the class an enum", "It hides the interface", 0, "Override marks implementation of inherited behavior."),
        q("m08", 12, "Do interfaces force implementers to share one constructor?", "No", "Yes, always", "Only when public", "Only for Compose", 0, "Interface implementation does not require a shared constructor."),

        q("m09", 1, "What is association?", "A general relationship between objects", "A Kotlin keyword for ownership", "An enum value", "A type of constructor", 0, "Association is the broad concept of objects being related."),
        q("m09", 2, "What most distinguishes aggregation from composition?", "Whether parts can live independently of the whole", "Whether Kotlin uses parentheses", "Whether a method is public", "Whether objects have names", 0, "Aggregation allows independent part lifetimes; composition implies stronger ownership."),
        q("m09", 3, "Does Kotlin syntax automatically enforce composition?", "No; ownership is a design/lifetime interpretation", "Yes, with `compose` keyword", "Yes, every List is composition", "Only for data classes", 0, "Language syntax alone does not enforce these modeling semantics."),
        q("m09", 4, "Why is a Team receiving existing Player values an aggregation-like design?", "Players can exist independently of the Team", "Players cannot have names", "Team subclasses Player", "The list is an interface", 0, "The parts are not created or owned exclusively by the whole."),
        q("m09", 5, "What is composition in a reading app example?", "A document manages its own private pages", "A teacher knows a course", "A function calls another function", "An enum lists colors", 0, "The document owns the pages collection and its lifetime."),
        q("m09", 6, "What does delegation do?", "Forwards a contract's behavior to a collaborator", "Makes every class a subtype", "Copies a database", "Converts a method to a property", 0, "Delegation reuses another object's implementation behind a contract."),
        q("m09", 7, "Which object handles write() for PrefixLogger if it does not override write?", "The PrefixLogger superclass", "The delegate Logger", "The Kotlin compiler's console", "No object; it is abstract", 1, "Kotlin forwards unoverridden interface members to the supplied delegate.", code = "class PrefixLogger(delegate: Logger) : Logger by delegate"),
        q("m09", 8, "How does composition differ from inheritance?", "Composition is has-a collaboration; inheritance is an is-a subtype", "They are exact synonyms", "Composition requires open classes", "Inheritance never reuses code", 0, "The relationship meaning is different."),
        q("m09", 9, "When should PrefixLogger override write?", "When it needs to add prefix behavior before forwarding", "Never, delegation cannot be customized", "Only during compilation", "When Logger is a data class", 0, "Explicit overrides can customize delegated operations."),
        q("m09", 10, "A teacher teaches a course. Which relationship is the broadest safe label?", "Association", "Composition always", "Delegation only", "Inheritance", 0, "The scenario states a general link without ownership assumptions."),
        q("m09", 11, "Why prefer composition when a subtype relationship is false?", "It reuses a collaborator without promising substitutability", "It makes all fields public", "It prevents collaborators", "It forces multiple inheritance", 0, "Composition avoids a misleading is-a contract."),
        q("m09", 12, "Which statement about aggregation and composition is accurate?", "They describe differing ownership/lifetime strength", "They are special Kotlin modifiers", "They describe overload signatures", "They are database isolation levels", 0, "The terms reason about object relationships."),

        q("m10", 1, "What does a Kotlin data class generate from primary-constructor properties?", "Value-oriented methods such as equals, copy, and component functions", "A Room database automatically", "An Android launcher", "An enum hierarchy", 0, "These generated operations support value-like data."),
        q("m10", 2, "What is an enum class best for?", "A finite set of named alternatives", "An unlimited set of user records", "A mutable list", "An abstract constructor", 0, "Enums model a closed set of constants."),
        q("m10", 3, "After this code, what is first.answer?", "Collaborating objects", "Objects with responsibilities", "It is null", "Compilation fails", 1, "copy returns a new Flashcard; it does not mutate first.", code = "data class Flashcard(val prompt: String, val answer: String)\nval first = Flashcard(\"OOP?\", \"Objects with responsibilities\")\nval revised = first.copy(answer = \"Collaborating objects\")"),
        q("m10", 4, "What does an object declaration create?", "A singleton instance", "An abstract class", "A new instance on every access", "A constructor overload only", 0, "Object declarations define one lazily initialized singleton."),
        q("m10", 5, "What is a companion object associated with?", "A class and its class-level conventions", "A particular list item only", "A Gradle project only", "A Room foreign key", 0, "Companions are singleton objects associated with their enclosing class."),
        q("m10", 6, "Is a Kotlin companion object literally a Java static field?", "No, it is an object associated with the class", "Yes, always", "It is a local lambda", "It is an enum entry", 0, "Companions provide class-associated members but are objects."),
        q("m10", 7, "Which is an appropriate ReviewState representation?", "enum class with NEW, LEARNING, MASTERED", "One arbitrary unvalidated string", "A database password", "A mutable global list", 0, "A finite state set fits an enum."),
        q("m10", 8, "What caution applies to a data class with a MutableList property?", "copy() is shallow; the referenced list may still be shared", "copy() deep-copies all nested objects", "Kotlin prohibits it", "The list becomes immutable", 0, "Generated copy is not a deep clone."),
        q("m10", 9, "When is a companion factory useful?", "When construction is meaningfully associated with the class", "For every top-level helper regardless of purpose", "To create global mutable state", "To override an enum", 0, "Companions can expose named class-related creation."),
        q("m10", 10, "Can an object declaration hold mutable state?", "Yes, which should be used carefully because it is shared", "No object has members", "Only in Java", "Only if abstract", 0, "All callers share the singleton state."),
        q("m10", 11, "Which property participates in generated data-class equality?", "A property in the primary constructor", "Any local variable in a method", "A comment", "A private package name", 0, "Generated equals uses primary-constructor properties."),
        q("m10", 12, "What is the best rule for utility declarations?", "Use top-level declarations when class association adds no clarity", "Every utility must be a companion", "Every utility must be an object", "Never use functions", 0, "Avoid unnecessary indirection."),

        q("m11", 1, "What does SRP ask of a module?", "Have one coherent reason to change", "Contain exactly one method", "Avoid all dependencies", "Be a singleton", 0, "SRP concerns responsibility and change, not method count."),
        q("m11", 2, "What does OCP encourage?", "Extend stable behavior without repeatedly editing its core", "Make every class closed to use", "Prohibit interfaces", "Put every feature in one class", 0, "A useful extension point allows new behavior with limited core changes."),
        q("m11", 3, "What is the Liskov Substitution Principle about?", "Subtypes honoring base-type expectations", "Interfaces having no methods", "Using only data classes", "Changing a pass threshold", 0, "Substitutability depends on preserving contract guarantees."),
        q("m11", 4, "What does ISP favor?", "Focused interfaces relevant to their clients", "One giant interface for all code", "No contracts", "Every implementation must add unused stubs", 0, "Clients should not depend on unused methods."),
        q("m11", 5, "What does DIP recommend for high-level policy?", "Depend on abstractions rather than concrete low-level details", "Depend directly on every storage implementation", "Avoid constructor injection", "Put SQL in Compose", 0, "Dependency inversion separates policy from concrete details."),
        q("m11", 6, "Why should Receipt formatting and persistence be separate responsibilities?", "They can change for different reasons", "SRP requires exactly two classes", "Formatting must always be global", "Persistence is not software", 0, "Distinct reasons to change suggest separate cohesive responsibilities."),
        q("m11", 7, "What substitution issue might violate LSP?", "A subtype rejects inputs that the base contract promises to accept", "A class has two methods", "An enum has three values", "A repository returns a Flow", 0, "Strengthening preconditions can break base-type callers."),
        q("m11", 8, "What does ReportService directly depend on in this code?", "A concrete disk implementation", "The ReportStore abstraction", "The Android Activity", "A global file path", 1, "Constructor injection makes the high-level service depend on the interface.", code = "interface ReportStore { fun load(): String }\nclass ReportService(private val store: ReportStore) {\n    fun report() = store.load().uppercase()\n}"),
        q("m11", 9, "What is a warning sign of over-applying SOLID?", "Adding abstractions without a real reason or variation point", "Separating independent responsibilities", "Using focused interfaces", "Testing substitutions", 0, "Principles guide useful designs; ceremony without benefit is not the goal."),
        q("m11", 10, "Why should a read-only client avoid depending on a save operation?", "ISP keeps the contract limited to what that client needs", "OCP prohibits reads", "LSP requires global state", "DIP removes interfaces", 0, "A smaller contract reduces accidental coupling."),
        q("m11", 11, "What makes an extension point useful under OCP?", "It supports a likely behavior variation through a stable contract", "It abstracts every local variable", "It never has implementations", "It edits the app manifest", 0, "Abstractions should be justified by real variation."),
        q("m11", 12, "Which principle is most directly about high-level code depending on abstractions?", "DIP", "SRP", "ISP", "LSP", 0, "Dependency Inversion Principle."),

        q("m12", 1, "Where should lesson completion persistence be coordinated?", "Through a repository called by the ViewModel", "Directly in a composable's SQL string", "In a global mutable variable only", "In an internet service", 0, "The repository owns storage access; the ViewModel coordinates UI actions."),
        q("m12", 2, "Why bundle lessons in an offline learning app?", "The core content remains available without network access", "Room requires internet", "It enables ad tracking", "It prevents local storage", 0, "Bundled content avoids a runtime network dependency."),
        q("m12", 3, "What should a Compose screen primarily do?", "Render observable state and dispatch user events", "Own schema migrations", "Randomize SQL table names", "Implement every grading rule", 0, "Presentation should remain focused on UI."),
        q("m12", 4, "What role does Room play in the sample architecture?", "A local persistence abstraction over SQLite", "A hosted cloud database", "A UI navigation system", "A Kotlin compiler plugin only", 0, "Room stores relational data in an on-device SQLite database."),
        q("m12", 5, "What value is emitted by this StateFlow initially?", "An empty list", "Null", "A database cursor", "No value until collected", 0, "The MutableStateFlow constructor sets the initial current value.", code = "private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())\nval lessons: StateFlow<List<Lesson>> = _lessons"),
        q("m12", 6, "Where should quiz grading logic live?", "In domain logic outside composables", "In a button's color lambda", "Only in XML", "In the app name resource", 0, "Separating logic makes behavior testable and consistent."),
        q("m12", 7, "Why use a transaction when resetting related learning rows?", "To keep multi-table changes consistent", "To upload content", "To turn a lesson into a module", "To change system font scale", 0, "Transactions ensure atomic database state changes."),
        q("m12", 8, "What does a repository abstraction enable?", "Storage changes without teaching the UI database details", "Automatic cloud sync", "Unlimited lesson generation", "Avoiding all tests", 0, "The UI depends on data operations rather than Room implementation."),
        q("m12", 9, "What should happen if the app is fully offline?", "Bundled lessons and local quiz features still work", "All screens must fail", "The app must request INTERNET", "The database is deleted", 0, "Offline-first content is part of the requirement."),
        q("m12", 10, "What should a historical result store to remain meaningful after content updates?", "Snapshots of question wording and answers", "Only a pointer to current mutable content", "A screen screenshot alone", "No question data", 0, "Snapshots preserve what the learner actually saw."),
        q("m12", 11, "Why run Room operations away from the main thread?", "Avoid blocking interaction and risking UI jank", "SQLite cannot use threads", "To create duplicate attempts", "To change the theme", 0, "Database work should not block rendering."),
        q("m12", 12, "Which separation is a sound app design?", "Content model, progress storage, and UI presentation have distinct responsibilities", "Every layer edits every table", "The ViewModel owns Compose nodes", "The repository draws buttons", 0, "Clear boundaries make the app easier to maintain and test.")
    )

    fun validate() {
        require(modules.map { it.id }.distinct().size == modules.size)
        require(lessons.map { it.id }.distinct().size == lessons.size)
        require(questions.map { it.id }.distinct().size == questions.size)
        val moduleIds = modules.map { it.id }.toSet()
        val lessonIds = lessons.map { it.id }.toSet()
        val lessonModules = lessons.associate { it.id to it.moduleId }
        require(lessons.all { it.moduleId in moduleIds })
        require(questions.all { it.moduleId in moduleIds && it.lessonId in lessonIds && lessonModules[it.lessonId] == it.moduleId })
        require(questions.all { it.options.size == 4 && it.options.distinct().size == 4 && it.correctIndex in 0..3 })
    }

    suspend fun import(database: OopDatabase) = withContext(Dispatchers.IO) {
        validate()
        database.withTransaction {
            val dao = database.contentDao()
            dao.putModules(modules)
            dao.putLessons(lessons.map {
                LessonEntity(it.id, it.moduleId, it.title, it.difficulty, it.objectives, it.readingMinutes,
                    it.explanation, it.example, it.code, it.codeExplanation, it.commonMistakes, it.takeaway)
            })
            dao.putQuestions(questions.map {
                QuestionEntity(it.id, it.moduleId, it.lessonId, it.difficulty, it.prompt, it.code, it.explanation, it.misconceptionTag)
            })
            val ids = questions.map { it.id }
            if (ids.isNotEmpty()) dao.deleteOptions(ids)
            dao.putOptions(questions.flatMap { question ->
                question.options.mapIndexed { index, text ->
                    QuestionOptionEntity("${question.id}-o${index + 1}", question.id, text, index == question.correctIndex, index)
                }
            })
            database.settingsDao().put(AppSettingEntity("seed_version", VERSION))
            database.settingsDao().put(AppSettingEntity("pass_threshold", database.settingsDao().get("pass_threshold")?.value ?: "70"))
            database.settingsDao().put(AppSettingEntity("theme", database.settingsDao().get("theme")?.value ?: "SYSTEM"))
            database.settingsDao().put(AppSettingEntity("text_scale", database.settingsDao().get("text_scale")?.value ?: "1.0"))
        }
    }
}
